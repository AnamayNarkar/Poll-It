import axiosInstance from "../axiosInstance";

const createPollRequest = async (pollData) => {
    try {
        const response = await axiosInstance.post('/poll/createPoll', pollData);
        return response.data;
    } catch (error) {
        window.alert(error.response.data.message);
        throw error.response.data;
    }
}

export default createPollRequest;