import React, { useEffect, useState } from 'react';
import '../styles/IndividualOptionComponentStyles.css';

const IndividualOptionComponent = ({
    option,
    toShowResults,
    castVote,
    voteCount,
    percentOfTotalVotes,
    isThisTheOptionUserVotedFor,
    isVotingDisabled
}) => {

    const [renderPercentage, setRenderPercentage] = useState(0);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setRenderPercentage(percentOfTotalVotes);
        }, 100);

        return () => clearTimeout(timeout);
    }, [percentOfTotalVotes]);

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
                        style={{ width: `${renderPercentage}%` }}
                    ></div>
                    <div className="percentageText">{`${renderPercentage.toFixed(1)}%`}</div>
                </div>
            </div>
        </div>
    ) : (
        <div
            className="individualOptionContainerForHiddenResults"
            onClick={() => {
                if (!isVotingDisabled) {
                    castVote(option);
                }
            }}
            style={{ pointerEvents: isVotingDisabled ? 'none' : 'auto' }}
        >
            <div className="selectOptionCircleForHiddenResults"></div>
            <div className="optionNameAndStatsForHiddenResults">
                <h4 className="individualOptionNameForHiddenResults">{option.option}</h4>
            </div>
        </div>
    );
};

export default IndividualOptionComponent;
