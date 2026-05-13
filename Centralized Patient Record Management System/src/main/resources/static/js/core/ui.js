export function setStatus(id, message, type = "idle") {
    const element = document.getElementById(id);
    if (!element) {
        return;
    }

    element.textContent = message;
    element.className = `status-line status-${type}`;
}

export function card(title, lines) {
    const article = document.createElement("article");
    article.className = "result-item";

    const heading = document.createElement("h4");
    heading.textContent = title;
    article.appendChild(heading);

    lines.forEach((line) => {
        const paragraph = document.createElement("p");
        paragraph.textContent = line;
        article.appendChild(paragraph);
    });

    return article;
}

export function renderList(containerId, items, emptyMessage) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    if (!items.length) {
        container.appendChild(card("No data", [emptyMessage]));
        return;
    }

    items.forEach((item) => container.appendChild(item));
}
