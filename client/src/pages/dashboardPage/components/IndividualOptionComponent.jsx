import React from 'react';
import '../styles/IndividualOptionComponentStyles.css';

const IndividualOptionComponent = ({
    option,
    toShowResults,
    castVote,
    voteCount,
    percentOfTotalVotes,
    isThisTheOptionUserVotedFor
}) => {
    return toShowResults ? (
        <div className="individualOptionContainerForResults">
            {isThisTheOptionUserVotedFor ? (
                <div className="selectedOptionCircleForResults"></div>
            ) : (
                <div className="selectOptionCircleForResults"></div>
            )}
            <div className="optionNameAndStatsForResults">
                <h4 className="individualOptionNameForResults">{option.option}</h4>
                <div className="optionStatsForResults">
                    <div
                        className="optionVotePercentageBar"
                        style={{ width: `${percentOfTotalVotes}%` }}
                    ></div>
                </div>
                <div className="optionVoteCount">{voteCount}</div>
            </div>
        </div>
    ) : (
        <div
            className="individualOptionContainerForHiddenResults"
            onClick={() => castVote(option)}
        >
            <div className="selectOptionCircleForHiddenResults"></div>
            <div className="optionNameAndStatsForHiddenResults">
                <h4 className="individualOptionNameForHiddenResults">{option.option}</h4>
            </div>
        </div>
    );
};

export default IndividualOptionComponent;