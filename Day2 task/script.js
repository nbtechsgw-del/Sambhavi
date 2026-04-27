const destinations = [
  {
    id: "goa",
    name: "Goa Beaches",
    city: "Goa",
    type: "beach",
    description: "Sunset coastlines, beach shacks, water sports, and lively evening spots.",
    image: "https://images.unsplash.com/photo-1518509562904-e7ef99cdcc86?auto=format&fit=crop&w=900&q=80",
    duration: "2-3 days",
    bestFor: "Couples and friends",
    route: [
      { stop: "Airport pickup", note: "25 min to hotel check-in", time: "09:00 AM" },
      { stop: "Calangute Beach", note: "Relax by the coast and explore cafes", time: "11:00 AM" },
      { stop: "Night market", note: "Shopping and local food stalls", time: "07:30 PM" }
    ],
    locationLabel: "Calangute Beach, Goa",
    mapSrc: "https://maps.google.com/maps?q=Calangute%20Beach%20Goa&t=&z=13&ie=UTF8&iwloc=&output=embed"
  },
  {
    id: "jaipur",
    name: "Jaipur Heritage",
    city: "Jaipur",
    type: "heritage",
    description: "Forts, royal architecture, colorful bazaars, and cultural walking routes.",
    image: "https://images.unsplash.com/photo-1599661046827-dacff0c0f09a?auto=format&fit=crop&w=900&q=80",
    duration: "2 days",
    bestFor: "History lovers",
    route: [
      { stop: "Amber Fort", note: "Morning fort tour and city lookout", time: "08:30 AM" },
      { stop: "Hawa Mahal", note: "Photo stop and heritage walk", time: "12:00 PM" },
      { stop: "Johari Bazaar", note: "Shopping and dinner route", time: "06:30 PM" }
    ],
    locationLabel: "Hawa Mahal, Jaipur",
    mapSrc: "https://maps.google.com/maps?q=Hawa%20Mahal%20Jaipur&t=&z=13&ie=UTF8&iwloc=&output=embed"
  },
  {
    id: "manali",
    name: "Manali Escape",
    city: "Manali",
    type: "nature",
    description: "Snow views, river valleys, fresh air, and scenic adventure experiences.",
    image: "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?auto=format&fit=crop&w=900&q=80",
    duration: "3-4 days",
    bestFor: "Adventure trips",
    route: [
      { stop: "Mall Road", note: "Start with cafes and rental pickups", time: "09:30 AM" },
      { stop: "Solang Valley", note: "Adventure activities and mountain views", time: "12:30 PM" },
      { stop: "Riverside camp", note: "Evening bonfire and dinner", time: "08:00 PM" }
    ],
    locationLabel: "Manali, Himachal Pradesh",
    mapSrc: "https://maps.google.com/maps?q=Manali%20Himachal%20Pradesh&t=&z=13&ie=UTF8&iwloc=&output=embed"
  },
  {
    id: "mumbai",
    name: "Mumbai Highlights",
    city: "Mumbai",
    type: "city",
    description: "Landmarks, sea-facing drives, shopping streets, and nonstop city energy.",
    image: "https://images.unsplash.com/photo-1529253355930-ddbe423a2ac7?auto=format&fit=crop&w=900&q=80",
    duration: "1-2 days",
    bestFor: "Fast city breaks",
    route: [
      { stop: "Gateway of India", note: "Kick off with waterfront views", time: "10:00 AM" },
      { stop: "Marine Drive", note: "Scenic drive and lunch stop", time: "02:00 PM" },
      { stop: "Colaba Causeway", note: "Street shopping and dinner", time: "07:00 PM" }
    ],
    locationLabel: "Gateway of India, Mumbai",
    mapSrc: "https://maps.google.com/maps?q=Gateway%20of%20India%20Mumbai&t=&z=13&ie=UTF8&iwloc=&output=embed"
  }
];

const hotels = [
  {
    destinationId: "goa",
    name: "Azure Coast Resort",
    price: "Rs 6,500 / night",
    rating: "4.7",
    distance: "600 m from beach",
    description: "Sea-view rooms, pool access, and walking distance from the beach."
  },
  {
    destinationId: "jaipur",
    name: "Amber Courtyard Hotel",
    price: "Rs 5,200 / night",
    rating: "4.6",
    distance: "1.2 km from Hawa Mahal",
    description: "Traditional interiors, rooftop dining, and guided heritage tours."
  },
  {
    destinationId: "manali",
    name: "Pine Mist Retreat",
    price: "Rs 4,800 / night",
    rating: "4.8",
    distance: "900 m from town center",
    description: "Mountain-facing rooms, bonfire evenings, and local adventure support."
  },
  {
    destinationId: "mumbai",
    name: "Harbor Grand",
    price: "Rs 7,400 / night",
    rating: "4.5",
    distance: "10 min to Gateway",
    description: "Comfort stay with city access, waterfront views, and quick transport links."
  }
];

const reviews = [
  {
    user: "Aarav",
    destinationId: "goa",
    rating: 5,
    text: "Easy to browse, and the place cards feel perfect for quick travel planning on phone."
  },
  {
    user: "Diya",
    destinationId: "jaipur",
    rating: 4.8,
    text: "The hotel and map sections are simple to use and help when planning a day trip."
  },
  {
    user: "Rohan",
    destinationId: "manali",
    rating: 4.9,
    text: "Loved how the app shows destination details and reviews in one compact layout."
  },
  {
    user: "Sara",
    destinationId: "mumbai",
    rating: 4.6,
    text: "Feels like a travel mobile app instead of a normal web page. Very clear and usable."
  }
];

const spotsGrid = document.getElementById("spotsGrid");
const hotelsGrid = document.getElementById("hotelsGrid");
const reviewsList = document.getElementById("reviewsList");
const searchInput = document.getElementById("searchInput");
const filterButtons = document.querySelectorAll(".filter-btn");
const mapSelect = document.getElementById("mapSelect");
const mapFrame = document.getElementById("mapFrame");
const mapCaption = document.getElementById("mapCaption");
const averageRating = document.getElementById("averageRating");
const reviewCount = document.getElementById("reviewCount");
const bottomNavItems = document.querySelectorAll(".nav-item");
const screenPanels = document.querySelectorAll(".screen-panel");
const selectedHotelName = document.getElementById("selectedHotelName");
const selectedDestinationName = document.getElementById("selectedDestinationName");
const selectedHotelPrice = document.getElementById("selectedHotelPrice");
const confirmBookingBtn = document.getElementById("confirmBookingBtn");
const bookingStatus = document.getElementById("bookingStatus");
const routeList = document.getElementById("routeList");
const reviewActionBtn = document.getElementById("reviewActionBtn");

let activeFilter = "all";
let searchTerm = "";
let activeScreen = "explore";
let selectedHotelId = hotels[0].name;

function formatTypeLabel(type) {
  return type.charAt(0).toUpperCase() + type.slice(1);
}

function getDestinationById(id) {
  return destinations.find((destination) => destination.id === id);
}

function renderHotels(filteredDestinations) {
  const destinationIds = filteredDestinations.map((destination) => destination.id);
  const visibleHotels = hotels.filter((hotel) => destinationIds.includes(hotel.destinationId));

  if (!visibleHotels.length) {
    hotelsGrid.innerHTML = '<div class="empty-state">No hotels found for this selection.</div>';
    return;
  }

  hotelsGrid.innerHTML = visibleHotels.map((hotel) => {
    const destination = getDestinationById(hotel.destinationId);
    const isSelected = selectedHotelId === hotel.name;
    return `
      <article class="hotel-card ${isSelected ? "is-selected" : ""}" data-hotel-name="${hotel.name}">
        <div class="hotel-body">
          <div class="hotel-top">
            <div class="hotel-meta">
              <span class="price-tag">${hotel.price}</span>
              <span class="rating-tag">${hotel.rating} rating</span>
            </div>
            <span class="trip-badge">${hotel.distance}</span>
          </div>
          <h3>${hotel.name}</h3>
          <p><strong>Near:</strong> ${destination.name}</p>
          <p>${hotel.description}</p>
          <button class="book-btn select-hotel-btn" type="button" data-hotel-name="${hotel.name}">Book now</button>
        </div>
      </article>
    `;
  }).join("");
}

function renderReviews(filteredDestinations) {
  const destinationIds = filteredDestinations.map((destination) => destination.id);
  const visibleReviews = reviews.filter((review) => destinationIds.includes(review.destinationId));

  if (!visibleReviews.length) {
    reviewsList.innerHTML = '<div class="empty-state">No reviews found for this selection.</div>';
    reviewCount.textContent = "0";
    averageRating.textContent = "0.0";
    return;
  }

  const avg = visibleReviews.reduce((sum, review) => sum + review.rating, 0) / visibleReviews.length;
  reviewCount.textContent = String(visibleReviews.length);
  averageRating.textContent = avg.toFixed(1);

  reviewsList.innerHTML = visibleReviews.map((review) => {
    const destination = getDestinationById(review.destinationId);
    return `
      <article class="review-card">
        <div class="review-meta">
          <span class="rating-tag">${review.rating.toFixed(1)} / 5</span>
          <span class="tag">${destination.city}</span>
        </div>
        <h3>${review.user}</h3>
        <p>${review.text}</p>
      </article>
    `;
  }).join("");
}

function renderDestinations() {
  const filteredDestinations = destinations.filter((destination) => {
    const matchesFilter = activeFilter === "all" || destination.type === activeFilter;
    const haystack = `${destination.name} ${destination.city} ${destination.description}`.toLowerCase();
    const matchesSearch = haystack.includes(searchTerm.toLowerCase());
    return matchesFilter && matchesSearch;
  });

  if (!filteredDestinations.length) {
    spotsGrid.innerHTML = '<div class="empty-state">No destinations matched your search.</div>';
    hotelsGrid.innerHTML = '<div class="empty-state">No hotels found for this selection.</div>';
    reviewsList.innerHTML = '<div class="empty-state">No reviews found for this selection.</div>';
    reviewCount.textContent = "0";
    averageRating.textContent = "0.0";
    return;
  }

  spotsGrid.innerHTML = filteredDestinations.map((destination) => `
    <article class="spot-card">
      <div class="spot-image" style="background-image: url('${destination.image}')"></div>
      <div class="spot-body">
        <div class="spot-meta">
          <div class="tag-row">
            <span class="tag">${formatTypeLabel(destination.type)}</span>
            <span class="price-tag">${destination.city}</span>
          </div>
          <span class="trip-badge">${destination.duration}</span>
        </div>
        <h3>${destination.name}</h3>
        <p>${destination.description}</p>
        <div class="tag-row">
          <span class="rating-tag">${destination.bestFor}</span>
        </div>
      </div>
    </article>
  `).join("");

  renderHotels(filteredDestinations);
  renderReviews(filteredDestinations);
}

function initializeMapSelector() {
  mapSelect.innerHTML = destinations.map((destination) => `
    <option value="${destination.id}">${destination.name}</option>
  `).join("");

  updateMap(destinations[0].id);
  renderRoute(destinations[0].id);
}

function updateMap(destinationId) {
  const destination = getDestinationById(destinationId);
  if (!destination) {
    return;
  }

  mapFrame.src = destination.mapSrc;
  mapCaption.textContent = `Previewing ${destination.locationLabel}. Best for ${destination.bestFor.toLowerCase()} and ideal for ${destination.duration.toLowerCase()} trips.`;
  renderRoute(destinationId);
}

function renderRoute(destinationId) {
  const destination = getDestinationById(destinationId);
  if (!destination) {
    routeList.innerHTML = "";
    return;
  }

  routeList.innerHTML = destination.route.map((step) => `
    <article class="route-stop">
      <strong>${step.stop}</strong>
      <p>${step.note}</p>
      <span>${step.time}</span>
    </article>
  `).join("");
}

function updateBookingSummary(hotelName) {
  const hotel = hotels.find((item) => item.name === hotelName) || hotels[0];
  const destination = getDestinationById(hotel.destinationId);
  selectedHotelId = hotel.name;
  selectedHotelName.textContent = hotel.name;
  selectedDestinationName.textContent = destination.name;
  selectedHotelPrice.textContent = hotel.price;
  bookingStatus.textContent = `Ready to reserve near ${destination.city}.`;
}

function setActiveScreen(screen) {
  activeScreen = screen;
  screenPanels.forEach((panel) => {
    panel.classList.toggle("active", panel.dataset.screen === screen);
  });
  bottomNavItems.forEach((item) => {
    item.classList.toggle("active", item.dataset.screenTarget === screen);
  });
}

searchInput.addEventListener("input", (event) => {
  searchTerm = event.target.value.trim();
  renderDestinations();
});

filterButtons.forEach((button) => {
  button.addEventListener("click", () => {
    activeFilter = button.dataset.filter;
    filterButtons.forEach((item) => item.classList.remove("active"));
    button.classList.add("active");
    renderDestinations();
  });
});

mapSelect.addEventListener("change", (event) => {
  updateMap(event.target.value);
});

bottomNavItems.forEach((button) => {
  button.addEventListener("click", () => {
    setActiveScreen(button.dataset.screenTarget);
  });
});

hotelsGrid.addEventListener("click", (event) => {
  const targetButton = event.target.closest(".select-hotel-btn");
  if (!targetButton) {
    return;
  }

  updateBookingSummary(targetButton.dataset.hotelName);
  renderDestinations();
  setActiveScreen("hotels");
});

confirmBookingBtn.addEventListener("click", () => {
  bookingStatus.textContent = `Booking confirmed for ${selectedHotelName.textContent}. Your stay details are ready.`;
});

reviewActionBtn.addEventListener("click", () => {
  setActiveScreen("hotels");
});

initializeMapSelector();
updateBookingSummary(selectedHotelId);
renderDestinations();
setActiveScreen(activeScreen);
