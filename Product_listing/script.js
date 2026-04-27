document.addEventListener("DOMContentLoaded", () => {
  const toastElement = document.getElementById("cartToast");
  const cartToast = toastElement ? new bootstrap.Toast(toastElement, { delay: 1800 }) : null;

  document.querySelectorAll(".add-cart-btn").forEach((button) => {
    button.addEventListener("click", () => {
      if (!button.classList.contains("added")) {
        button.classList.add("added");
        button.textContent = "Added";
      }

      if (cartToast) {
        cartToast.show();
      }
    });
  });
});
