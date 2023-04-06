const BASE_URL = 'http://localhost:8080/testServlet_war';

export async function sendRequest(url, method, data, token) {
    console.log(method + " " + url)
    let headers = ""
    if (token) headers = "Bearer " + token;
    const request = {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": `${headers}`
        }
    };
    if (data) request.body = JSON.stringify(data)

    const response = await fetch(BASE_URL + url, request);
    if (response.status === 401) {
        const refreshToken = localStorage.getItem('RefreshToken');
        const responseTokens = await fetch(BASE_URL + '/refresh-token', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${refreshToken}`
            }
        });
        if (responseTokens.status !== 401) {
            const newTokens = await responseTokens.json();
            localStorage.setItem('AccessToken', newTokens['AccessToken']);
            localStorage.setItem('RefreshToken', newTokens['RefreshToken']);

            return await sendRequest(url, method, data, newTokens['AccessToken']);
        } else {
            return responseTokens;
        }
    } else {
        return response;
    }
}