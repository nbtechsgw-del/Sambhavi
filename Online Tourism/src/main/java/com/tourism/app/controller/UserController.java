package com.tourism.app.controller;

import com.tourism.app.model.Booking;
import com.tourism.app.model.Feedback;
import com.tourism.app.model.Hotel;
import com.tourism.app.model.HotelRoomType;
import com.tourism.app.model.Payment;
import com.tourism.app.model.TourPackage;
import com.tourism.app.model.User;
import com.tourism.app.repository.BookingRepository;
import com.tourism.app.repository.FeedbackRepository;
import com.tourism.app.repository.HotelBookingRepository;
import com.tourism.app.repository.HotelRepository;
import com.tourism.app.repository.HotelRoomTypeRepository;
import com.tourism.app.repository.PaymentRepository;
import com.tourism.app.repository.TourPackageRepository;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {
    private final TourPackageRepository tourPackageRepository;
    private final HotelRepository hotelRepository;
    private final HotelRoomTypeRepository hotelRoomTypeRepository;
    private final HotelBookingRepository hotelBookingRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final FeedbackRepository feedbackRepository;

    public UserController(TourPackageRepository tourPackageRepository, HotelRepository hotelRepository, HotelRoomTypeRepository hotelRoomTypeRepository,
                          HotelBookingRepository hotelBookingRepository, BookingRepository bookingRepository,
                          PaymentRepository paymentRepository, FeedbackRepository feedbackRepository) {
        this.tourPackageRepository = tourPackageRepository;
        this.hotelRepository = hotelRepository;
        this.hotelRoomTypeRepository = hotelRoomTypeRepository;
        this.hotelBookingRepository = hotelBookingRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String error, @RequestParam(required = false) String paid,
                            HttpSession session, Model model) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("packages", tourPackageRepository.findAll());
        model.addAttribute("hotels", hotelRepository.findAll());
        model.addAttribute("bookings", bookingRepository.findByUserOrderByIdDesc(user));
        model.addAttribute("hotelBookings", hotelBookingRepository.findByUserOrderByIdDesc(user));
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("error", error);
        model.addAttribute("paid", paid);
        return "user/dashboard";
    }

    @PostMapping("/book-hotel")
    @Transactional
    public String bookHotel(@RequestParam Long hotelId, @RequestParam Long roomTypeId, @RequestParam String checkInDate,
                            @RequestParam String checkOutDate, @RequestParam Integer rooms,
                            @RequestParam Integer guests, HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();
        HotelRoomType selectedRoomType = hotelRoomTypeRepository.findById(roomTypeId).orElseThrow();
        if (!selectedRoomType.getHotel().getId().equals(hotel.getId())) {
            return "redirect:/user/dashboard?error=Please select a valid room type for this hotel";
        }
        if (rooms > selectedRoomType.getRoomsAvailable()) {
            return "redirect:/user/dashboard?error=Only " + selectedRoomType.getRoomsAvailable() + " "
                    + selectedRoomType.getTypeName() + " rooms are available";
        }
        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);
        long nights = Math.max(1, ChronoUnit.DAYS.between(checkIn, checkOut));

        var hotelBooking = new com.tourism.app.model.HotelBooking();
        hotelBooking.setUser(user);
        hotelBooking.setHotel(hotel);
        hotelBooking.setCheckInDate(checkIn);
        hotelBooking.setCheckOutDate(checkOut);
        hotelBooking.setRoomTypeName(selectedRoomType.getTypeName());
        hotelBooking.setRoomPricePerNight(selectedRoomType.getPricePerNight());
        hotelBooking.setRooms(rooms);
        hotelBooking.setGuests(guests);
        hotelBooking.setTotalAmount(selectedRoomType.getPricePerNight().multiply(BigDecimal.valueOf(nights)).multiply(BigDecimal.valueOf(rooms)));
        selectedRoomType.setRoomsAvailable(selectedRoomType.getRoomsAvailable() - rooms);
        hotel.setRoomsAvailable(Math.max(0, hotel.getRoomsAvailable() - rooms));
        hotelRepository.save(hotel);
        hotelRoomTypeRepository.save(selectedRoomType);
        hotelBookingRepository.save(hotelBooking);
        return "redirect:/user/dashboard#hotel-bookings";
    }

    @PostMapping("/book")
    @Transactional
    public String book(@RequestParam Long packageId, @RequestParam(required = false) Long hotelId,
                       @RequestParam String travelDate, @RequestParam Integer travellers, HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        TourPackage tourPackage = tourPackageRepository.findById(packageId).orElseThrow();
        if (travellers > tourPackage.getAvailableSeats()) {
            return "redirect:/user/dashboard?error=Only " + tourPackage.getAvailableSeats() + " seats are available";
        }
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTourPackage(tourPackage);
        booking.setTravelDate(java.time.LocalDate.parse(travelDate));
        booking.setTravellers(travellers);
        booking.setTotalAmount(tourPackage.getPrice().multiply(BigDecimal.valueOf(travellers)));
        if (hotelId != null) {
            hotelRepository.findById(hotelId).ifPresent(booking::setHotel);
        }
        tourPackage.setAvailableSeats(tourPackage.getAvailableSeats() - travellers);
        tourPackageRepository.save(tourPackage);
        Booking savedBooking = bookingRepository.save(booking);
        return "redirect:/user/bookings/" + savedBooking.getId() + "/pay";
    }

    @GetMapping("/bookings/{id}/pay")
    public String pay(@PathVariable Long id, @RequestParam(required = false) String method,
                      HttpSession session, Model model) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        Booking booking = bookingRepository.findById(id).orElseThrow();
        if (!booking.getUser().getId().equals(user.getId())) {
            return "redirect:/user/dashboard";
        }
        Payment payment = paymentRepository.findByBookingId(id).orElseGet(() -> {
            Payment newPayment = new Payment();
            newPayment.setBooking(booking);
            newPayment.setAmount(booking.getTotalAmount());
            newPayment.setTransactionId("PENDING-" + booking.getId());
            newPayment.setStatus(Payment.Status.PENDING);
            return newPayment;
        });
        if (payment.getStatus() == Payment.Status.SUCCESS) {
            return "redirect:/user/bookings/" + id + "/invoice";
        }
        payment.setGatewayProvider("LOCAL");
        payment.setGatewayOrderId("LOCAL-ORDER-" + booking.getId());
        paymentRepository.save(payment);
        model.addAttribute("booking", booking);
        model.addAttribute("payment", payment);
        model.addAttribute("selectedMethod", normalizePaymentMethod(method));
        return "user/payment";
    }

    @PostMapping("/payments/{id}/otp/send")
    @Transactional
    public String sendPaymentOtp(@PathVariable Long id, @RequestParam String paymentMethod,
                                 @RequestParam(required = false) String cardName,
                                 @RequestParam(required = false) String cardNumber,
                                 @RequestParam(required = false) String cardExpiry,
                                 @RequestParam(required = false) String cardCvv,
                                 @RequestParam(required = false) String upiId,
                                 HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        Payment payment = paymentRepository.findById(id).orElseThrow();
        if (!payment.getBooking().getUser().getId().equals(user.getId())) {
            return "redirect:/user/dashboard";
        }
        String normalizedMethod = paymentMethod == null ? "" : paymentMethod.trim().toUpperCase();
        if (!isLocalPaymentValid(normalizedMethod, cardName, cardNumber, cardExpiry, cardCvv, upiId)) {
            return "redirect:/user/bookings/" + payment.getBooking().getId() + "/pay?method=" + normalizedMethod;
        }

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        String transactionId = "LOCAL-" + normalizedMethod + "-" + System.currentTimeMillis();
        session.setAttribute(otpSessionKey(id), otp);
        session.setAttribute(paymentMethodSessionKey(id), normalizedMethod);
        session.setAttribute(transactionSessionKey(id), transactionId);

        payment.setGatewayProvider("LOCAL_" + normalizedMethod);
        payment.setTransactionId("OTP-PENDING-" + id);
        payment.setStatus(Payment.Status.PENDING);
        paymentRepository.save(payment);
        return "redirect:/user/payments/" + id + "/otp";
    }

    @GetMapping("/payments/{id}/otp")
    public String otpPage(@PathVariable Long id, HttpSession session, Model model) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        Payment payment = paymentRepository.findById(id).orElseThrow();
        if (!payment.getBooking().getUser().getId().equals(user.getId())) {
            return "redirect:/user/dashboard";
        }
        Object otp = session.getAttribute(otpSessionKey(id));
        if (otp == null) {
            return "redirect:/user/bookings/" + payment.getBooking().getId() + "/pay";
        }
        model.addAttribute("booking", payment.getBooking());
        model.addAttribute("payment", payment);
        model.addAttribute("maskedPhone", maskPhone(user.getPhone()));
        model.addAttribute("demoOtp", otp);
        return "user/payment-otp";
    }

    @PostMapping("/payments/{id}/success")
    @Transactional
    public String paymentSuccess(@PathVariable Long id, @RequestParam String otp, HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        Payment payment = paymentRepository.findById(id).orElseThrow();
        if (!payment.getBooking().getUser().getId().equals(user.getId())) {
            return "redirect:/user/dashboard";
        }

        Object expectedOtp = session.getAttribute(otpSessionKey(id));
        Object transactionIdValue = session.getAttribute(transactionSessionKey(id));
        Object methodValue = session.getAttribute(paymentMethodSessionKey(id));
        if (expectedOtp == null || !expectedOtp.toString().equals(otp.trim())) {
            return "redirect:/user/payments/" + id + "/otp?error=invalid";
        }

        String normalizedMethod = methodValue == null ? "LOCAL" : methodValue.toString();
        String transactionId = transactionIdValue == null ? "LOCAL-" + System.currentTimeMillis() : transactionIdValue.toString();
        payment.setGatewayProvider("LOCAL_" + normalizedMethod);
        payment.setGatewayPaymentId(transactionId);
        payment.setGatewaySignature(null);
        payment.setTransactionId(transactionId);
        payment.setPaidAt(LocalDateTime.now());
        payment.setStatus(Payment.Status.SUCCESS);
        Booking booking = payment.getBooking();
        booking.setStatus(Booking.Status.PAID);
        bookingRepository.save(booking);
        paymentRepository.save(payment);
        session.removeAttribute(otpSessionKey(id));
        session.removeAttribute(paymentMethodSessionKey(id));
        session.removeAttribute(transactionSessionKey(id));
        return "redirect:/user/dashboard?paid=true";
    }

    @GetMapping("/bookings/{id}/invoice")
    public String invoice(@PathVariable Long id, HttpSession session, Model model) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        Booking booking = bookingRepository.findById(id).orElseThrow();
        if (!booking.getUser().getId().equals(user.getId())) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("payment", paymentRepository.findByBookingId(id).orElse(null));
        return "user/invoice";
    }

    @GetMapping("/bookings/{id}/cancel")
    @Transactional
    public String cancel(@PathVariable Long id, HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        bookingRepository.findById(id).ifPresent(booking -> {
            if (booking.getUser().getId().equals(user.getId()) && booking.getStatus() != Booking.Status.CANCELLED) {
                TourPackage tourPackage = booking.getTourPackage();
                tourPackage.setAvailableSeats(tourPackage.getAvailableSeats() + booking.getTravellers());
                tourPackageRepository.save(tourPackage);
                booking.setStatus(Booking.Status.CANCELLED);
                bookingRepository.save(booking);
            }
        });
        return "redirect:/user/dashboard";
    }

    @PostMapping("/feedback")
    public String feedback(@RequestParam Integer rating, @RequestParam String subject, @RequestParam String message, HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/customer/login";
        }
        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setRating(rating);
        feedback.setSubject(subject);
        feedback.setMessage(message);
        feedbackRepository.save(feedback);
        return "redirect:/user/dashboard";
    }

    private User currentUser(HttpSession session) {
        Object currentUser = session.getAttribute("loggedInUser");
        return currentUser instanceof User user && user.getRole() == User.Role.USER ? user : null;
    }

    private String normalizePaymentMethod(String method) {
        if (method == null) {
            return null;
        }
        String normalized = method.trim().toUpperCase();
        return "CARD".equals(normalized) || "UPI".equals(normalized) ? normalized : null;
    }

    private boolean isLocalPaymentValid(String paymentMethod, String cardName, String cardNumber,
                                        String cardExpiry, String cardCvv, String upiId) {
        if ("CARD".equals(paymentMethod)) {
            String digits = cardNumber == null ? "" : cardNumber.replaceAll("\\D", "");
            String cvv = cardCvv == null ? "" : cardCvv.trim();
            return cardName != null && !cardName.isBlank()
                    && digits.length() >= 12 && digits.length() <= 19
                    && cardExpiry != null && !cardExpiry.isBlank()
                    && cvv.matches("\\d{3,4}");
        }
        if ("UPI".equals(paymentMethod)) {
            return upiId != null && upiId.trim().matches("[A-Za-z0-9._-]{2,}@[A-Za-z]{2,}");
        }
        return false;
    }

    private String otpSessionKey(Long paymentId) {
        return "paymentOtp-" + paymentId;
    }

    private String paymentMethodSessionKey(Long paymentId) {
        return "paymentMethod-" + paymentId;
    }

    private String transactionSessionKey(Long paymentId) {
        return "paymentTransaction-" + paymentId;
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return "your registered mobile number";
        }
        String digits = phone.replaceAll("\\D", "");
        if (digits.length() <= 4) {
            return phone;
        }
        return "******" + digits.substring(digits.length() - 4);
    }
}
