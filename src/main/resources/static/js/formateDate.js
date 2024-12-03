
    document.addEventListener('DOMContentLoaded', function date() {
    const formatTimeAgo = (dateString) => {
    const now = new Date();
    const date = new Date(dateString);
    const diffInSeconds = Math.floor((now - date) / 1000);
    const intervals = {
    year: 31536000,
    month: 2592000,
    week: 604800,
    day: 86400,
    hour: 3600,
    minute: 60,
    second: 1,
};

    for (const key in intervals) {
    const interval = Math.floor(diffInSeconds / intervals[key]);
    if (interval >= 1) {
    return `${interval} ${key}${interval > 1 ? 's' : ''} ago`;
}
}
    return 'just now';
};

    document.querySelectorAll('[data-createdat]').forEach((element) => {
    const createdAt = element.getAttribute('data-createdat');
    const formattedTime = formatTimeAgo(createdAt);
    element.textContent = formattedTime;
});
});

