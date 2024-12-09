import axiosInstanceWithNoErrorChecks from "../axiosInstanceWithNoErrorChecks";

const voteForPoll = async (pollId, optionId) => {

    try {
        const response = await axiosInstanceWithNoErrorChecks.post(`/poll/vote/${pollId}/${optionId}`);
        return response;
    } catch (error) {
        return error.response;
    }

}

export default voteForPoll;