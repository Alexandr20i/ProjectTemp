function toggleDarkMode() {
    // Проверяем, включена ли уже темная тема
    if (!document.body.classList.contains('dark-mode')) {
        document.body.classList.add('dark-mode');
    } else {
        document.body.classList.remove('dark-mode');
    }
}