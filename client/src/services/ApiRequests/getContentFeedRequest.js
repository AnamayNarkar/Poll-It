import axiosInstance from "../axiosInstance";

const getContentFeedRequest = async (feedType, param, page, limit) => {

    if (feedType === "home") {
        try {
            const response = await axiosInstance.get(`/feed/${feedType}/${limit}`, {});
            return response;
        } catch (err) {
            return err.response;
        }
    }

}

export default getContentFeedRequest;