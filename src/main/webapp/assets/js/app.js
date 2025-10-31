// Mid-level helpers. No server logic touched.
document.addEventListener("DOMContentLoaded", () => {
    // 1) Client-side search on listings by title
    const grid = document.getElementById("listingGrid");
    const search = document.getElementById("listingSearch");
    if (grid && search) {
        const items = Array.from(grid.children);
        function apply() {
            const q = (search.value || "").trim().toLowerCase();
            items.forEach(li => {
                const t = (li.dataset.title || "").toLowerCase();
                li.style.display = t.includes(q) ? "" : "none";
            });
        }
        search.addEventListener("input", apply);
    }

    // 2) Anti double-submit
    document.querySelectorAll("form").forEach(f => {
        f.addEventListener("submit", e => {
            if (!f.checkValidity()) { e.preventDefault(); return; }
            const btn = f.querySelector("button[type=submit],input[type=submit]");
            if (btn) btn.disabled = true;
        });
    });
});
