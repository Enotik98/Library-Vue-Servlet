export function formatDate(time) {
    const date = new Date(time);
    return date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear()
}

export function getBookName(bookId, books) {
    const book = books.find(book => book.id === bookId)
    return book ? book.name : 'невідома книга'
}

export function getUsername(userId, users) {
    const user = users.find(user => user.id === userId)
    return user ? user.username : 'невідомий клієнт'
}

export function getDateForRequest(time) {
    const date = new Date(time);
    return date.getFullYear() + '-' + (date.getMonth() + 1) + "-" + date.getDate();
}

export function formatStatus(status) {
    switch (status) {
        case 'WAITING':
            return 'Очікує';
        case 'ISSUED':
            return 'Видана';
        case 'RETURNED':
            return 'Повернута';
        default:
            return 'unknown';
    }
}
export function formatType(type){
    switch (type) {
        case 'ROOM':
            return 'Зала';
        case 'SUBSCRIPTION':
            return 'Абонемент'
        default:
            return 'unknown';
    }
}
export function checkToken(){
    return  !!localStorage.getItem("AccessToken");
}