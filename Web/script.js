// Toggle documentation sidebar
function toggleDocs() {
  const sidebar = document.getElementById("sidebar");
  const toggleText = document.getElementById("docs-toggle-text");

  sidebar.classList.toggle("open");
  toggleText.textContent = sidebar.classList.contains("open")
    ? "Hide Docs"
    : "Show Docs";
}

// Run Happy code
async function run() {
  const code = document.getElementById("code").value;

  const res = await fetch("/run", {
    method: "POST",
    body: code
  });

  document.getElementById("output").textContent = await res.text();
}

// Clear code and output
function clear() {
  document.getElementById("code").value = "";
  document.getElementById("output").textContent = "";
}

// Wire up buttons
document.getElementById("toggle-docs").addEventListener("click", toggleDocs);
document.getElementById("run-btn").addEventListener("click", run);
document.getElementById("clear-btn").addEventListener("click", clear);

// Copy buttons
document.querySelectorAll(".copy-btn").forEach(button => {
  button.addEventListener("click", () => {
    const pre = button.nextElementSibling;
    navigator.clipboard.writeText(pre.textContent);

    button.textContent = "Copied!";
    setTimeout(() => (button.textContent = "Copy"), 1000);
  });
});

// Example toggles
document.querySelectorAll(".example-header").forEach(header => {
  header.addEventListener("click", () => {
    header.parentElement.classList.toggle("open");
  });
});