package com.tourism.app.controller;

import com.tourism.app.model.Booking;
import com.tourism.app.model.Destination;
import com.tourism.app.model.Hotel;
import com.tourism.app.model.HotelRoomType;
import com.tourism.app.model.Payment;
import com.tourism.app.model.TourPackage;
import com.tourism.app.model.User;
import com.tourism.app.repository.BookingRepository;
import com.tourism.app.repository.DestinationRepository;
import com.tourism.app.repository.FeedbackRepository;
import com.tourism.app.repository.HotelBookingRepository;
import com.tourism.app.repository.HotelRepository;
import com.tourism.app.repository.HotelReviewRepository;
import com.tourism.app.repository.HotelRoomTypeRepository;
import com.tourism.app.repository.PaymentRepository;
import com.tourism.app.repository.TourPackageRepository;
import com.tourism.app.repository.UserRepository;
import com.tourism.app.service.PaymentGatewayService;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final TourPackageRepository tourPackageRepository;
    private final DestinationRepository destinationRepository;
    private final HotelRepository hotelRepository;
    private final HotelBookingRepository hotelBookingRepository;
    private final HotelReviewRepository hotelReviewRepository;
    private final HotelRoomTypeRepository hotelRoomTypeRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final FeedbackRepository feedbackRepository;
    private final PaymentGatewayService paymentGatewayService;

    public AdminController(UserRepository userRepository, TourPackageRepository tourPackageRepository, DestinationRepository destinationRepository,
                           HotelRepository hotelRepository, HotelBookingRepository hotelBookingRepository, HotelReviewRepository hotelReviewRepository,
                           HotelRoomTypeRepository hotelRoomTypeRepository, BookingRepository bookingRepository, PaymentRepository paymentRepository,
                           FeedbackRepository feedbackRepository, PaymentGatewayService paymentGatewayService) {
        this.userRepository = userRepository;
        this.tourPackageRepository = tourPackageRepository;
        this.destinationRepository = destinationRepository;
        this.hotelRepository = hotelRepository;
        this.hotelBookingRepository = hotelBookingRepository;
        this.hotelReviewRepository = hotelReviewRepository;
        this.hotelRoomTypeRepository = hotelRoomTypeRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.feedbackRepository = feedbackRepository;
        this.paymentGatewayService = paymentGatewayService;
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("users", userRepository.count());
        model.addAttribute("packages", tourPackageRepository.count());
        model.addAttribute("destinations", destinationRepository.count());
        model.addAttribute("hotels", hotelRepository.count());
        model.addAttribute("bookings", bookingRepository.count());
        model.addAttribute("payments", paymentRepository.findAll());
        model.addAttribute("feedback", feedbackRepository.findAll());
        model.addAttribute("bookingList", bookingRepository.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/destinations")
    public String destinationForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("destination", new Destination());
        model.addAttribute("destinations", destinationRepository.findAll());
        model.addAttribute("error", session.getAttribute("adminError"));
        session.removeAttribute("adminError");
        return "admin/destinations";
    }

    @PostMapping("/destinations")
    public String saveDestination(@ModelAttribute Destination destination, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        destinationRepository.save(destination);
        return "redirect:/admin/destinations";
    }

    @GetMapping("/destinations/edit/{id}")
    public String editDestination(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("destination", destinationRepository.findById(id).orElse(new Destination()));
        model.addAttribute("destinations", destinationRepository.findAll());
        return "admin/destinations";
    }

    @PostMapping("/destinations/delete/{id}")
    public String deleteDestination(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        try {
            destinationRepository.deleteById(id);
        } catch (Exception ex) {
            session.setAttribute("adminError", "Destination could not be deleted because it is still linked to other records.");
        }
        return "redirect:/admin/destinations";
    }

    @GetMapping("/packages")
    public String packageForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("packageItem", new TourPackage());
        try {
            model.addAttribute("packages", tourPackageRepository.findAll());
        } catch (Exception ex) {
            model.addAttribute("packages", Collections.emptyList());
            model.addAttribute("error", "Packages could not be loaded. Please check the tour_packages table.");
            return "admin/packages";
        }
        model.addAttribute("error", session.getAttribute("adminError"));
        session.removeAttribute("adminError");
        return "admin/packages";
    }

    @PostMapping("/packages")
    public String savePackage(@ModelAttribute("packageItem") TourPackage tourPackage, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        try {
            tourPackageRepository.save(tourPackage);
        } catch (Exception ex) {
            session.setAttribute("adminError", "Package could not be saved. Please check all required fields.");
        }
        return "redirect:/admin/packages";
    }

    @GetMapping("/packages/edit/{id}")
    public String editPackage(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("packageItem", tourPackageRepository.findById(id).orElse(new TourPackage()));
        try {
            model.addAttribute("packages", tourPackageRepository.findAll());
        } catch (Exception ex) {
            model.addAttribute("packages", Collections.emptyList());
            model.addAttribute("error", "Packages could not be loaded. Please check the tour_packages table.");
        }
        return "admin/packages";
    }

    @GetMapping("/packages/delete/{id}")
    public String oldDeletePackageLink(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        session.setAttribute("adminError", "Please use the red Delete button to remove a package.");
        return "redirect:/admin/packages";
    }

    @PostMapping("/packages/delete/{id}")
    @Transactional
    public String deletePackage(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        try {
            tourPackageRepository.findById(id).ifPresent(tourPackage -> {
                bookingRepository.findByTourPackage(tourPackage).forEach(booking -> {
                    paymentRepository.findByBookingId(booking.getId()).ifPresent(paymentRepository::delete);
                    paymentRepository.flush();
                    bookingRepository.delete(booking);
                    bookingRepository.flush();
                });
                tourPackageRepository.delete(tourPackage);
            });
        } catch (Exception ex) {
            session.setAttribute("adminError", "Package could not be deleted because it is still linked to another record.");
        }
        return "redirect:/admin/packages";
    }

    @GetMapping("/hotels")
    public String hotelForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("hotels", hotelRepository.findAll());
        model.addAttribute("error", session.getAttribute("adminError"));
        session.removeAttribute("adminError");
        return "admin/hotels";
    }

    @PostMapping("/hotels")
    @Transactional
    public String saveHotel(@ModelAttribute Hotel hotel,
                            @RequestParam(required = false) List<String> roomTypeNames,
                            @RequestParam(required = false) List<BigDecimal> roomTypePrices,
                            @RequestParam(required = false) List<Integer> roomTypeRooms,
                            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        List<HotelRoomType> roomTypes = buildRoomTypes(roomTypeNames, roomTypePrices, roomTypeRooms);
        if (!roomTypes.isEmpty()) {
            HotelRoomType firstRoomType = roomTypes.get(0);
            hotel.setRoomType(firstRoomType.getTypeName());
            hotel.setPricePerNight(firstRoomType.getPricePerNight());
            hotel.setRoomsAvailable(roomTypes.stream()
                    .map(HotelRoomType::getRoomsAvailable)
                    .filter(rooms -> rooms != null)
                    .reduce(0, Integer::sum));
        }
        Hotel savedHotel = hotelRepository.save(hotel);
        hotelRoomTypeRepository.deleteByHotel(savedHotel);
        roomTypes.forEach(roomType -> {
            roomType.setHotel(savedHotel);
            hotelRoomTypeRepository.save(roomType);
        });
        return "redirect:/admin/hotels";
    }

    @GetMapping("/hotels/edit/{id}")
    public String editHotel(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("hotel", hotelRepository.findById(id).orElse(new Hotel()));
        model.addAttribute("hotels", hotelRepository.findAll());
        return "admin/hotels";
    }

    @PostMapping("/hotels/delete/{id}")
    @Transactional
    public String deleteHotel(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        hotelRepository.findById(id).ifPresent(hotel -> {
            bookingRepository.findByHotel(hotel).forEach(booking -> {
                booking.setHotel(null);
                bookingRepository.save(booking);
            });
            hotelRoomTypeRepository.deleteByHotel(hotel);
            hotelBookingRepository.deleteByHotel(hotel);
            hotelReviewRepository.deleteByHotel(hotel);
            hotelRepository.delete(hotel);
        });
        return "redirect:/admin/hotels";
    }

    private List<HotelRoomType> buildRoomTypes(List<String> names, List<BigDecimal> prices, List<Integer> rooms) {
        if (names == null || prices == null || rooms == null) {
            return Collections.emptyList();
        }
        int rowCount = Math.min(names.size(), Math.min(prices.size(), rooms.size()));
        return java.util.stream.IntStream.range(0, rowCount)
                .filter(index -> names.get(index) != null && !names.get(index).isBlank()
                        && prices.get(index) != null
                        && rooms.get(index) != null
                        && rooms.get(index) >= 0)
                .mapToObj(index -> {
                    HotelRoomType roomType = new HotelRoomType();
                    roomType.setTypeName(names.get(index).trim());
                    roomType.setPricePerNight(prices.get(index));
                    roomType.setRoomsAvailable(rooms.get(index));
                    return roomType;
                })
                .toList();
    }

    @GetMapping("/bookings/{id}/approve")
    public String approveBooking(@PathVariable Long id) {
        bookingRepository.findById(id).ifPresent(booking -> {
            booking.setStatus(Booking.Status.APPROVED);
            bookingRepository.save(booking);
        });
        return "redirect:/admin";
    }

    @GetMapping("/bookings/{id}/cancel")
    @Transactional
    public String cancelBooking(@PathVariable Long id) {
        bookingRepository.findById(id).ifPresent(booking -> {
            cancelAndRestoreSeats(booking);
        });
        return "redirect:/admin";
    }

    @GetMapping("/payments/{id}/refund")
    @Transactional
    public String refundPayment(@PathVariable Long id) {
        paymentRepository.findById(id).ifPresent(payment -> {
            if (payment.getStatus() == Payment.Status.SUCCESS || payment.getRefundId() != null) {
                String refundId = paymentGatewayService.refund(payment.getGatewayPaymentId(), payment.getAmount());
                if (refundId == null || refundId.startsWith("FAILED")) {
                    refundId = "LOCAL-REFUND-" + System.currentTimeMillis();
                }
                payment.setRefundId(refundId);
                payment.setStatus(Payment.Status.REFUNDED);
                cancelAndRestoreSeats(payment.getBooking());
                paymentRepository.save(payment);
            }
        });
        return "redirect:/admin";
    }

    private void cancelAndRestoreSeats(Booking booking) {
        if (booking.getStatus() != Booking.Status.CANCELLED) {
            TourPackage tourPackage = booking.getTourPackage();
            tourPackage.setAvailableSeats(tourPackage.getAvailableSeats() + booking.getTravellers());
            tourPackageRepository.save(tourPackage);
            booking.setStatus(Booking.Status.CANCELLED);
            bookingRepository.save(booking);
        }
    }

    private boolean isAdmin(HttpSession session) {
        Object currentUser = session.getAttribute("loggedInUser");
        return currentUser instanceof User user && user.getRole() == User.Role.ADMIN;
    }
}
