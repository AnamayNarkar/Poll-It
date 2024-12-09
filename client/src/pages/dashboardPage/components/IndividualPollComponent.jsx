import React, { useEffect, useState } from 'react';
import '../styles/IndividualPollComponentStyles.css';
import IndividualOptionComponent from './IndividualOptionComponent.jsx';
import voteForPoll from '../../../services/ApiRequests/voteForPollRequest.js';

const IndividualPollComponent = ({ poll }) => {
    const [totalVotes, setTotalVotes] = useState(
        poll.options.reduce((acc, option) => acc + option.voteCount, 0)
    );
    const [toShowResults, setToShowResults] = useState(false);

    useEffect(() => {
        if (
            poll.expirationDateTime < new Date().toISOString() || poll.hasUserVotedForThisPoll
        ) {
            setToShowResults(true);
        }
    }, [poll]);

    async function castVote(option) {
        const response = await voteForPoll(poll.id, option.id);
        if (response.status >= 200 && response.status < 300) {
            poll.options.forEach((pollOption) => {
                if (pollOption.id === option.id) {
                    option.voteCount += 1;
                }
            });
            setTotalVotes(totalVotes + 1);
            poll.hasUserVotedForThisPoll = true;
            poll.optionIdVotedFor = option.id;
            setToShowResults(true);
        } else {
            window.alert('Error casting vote');
        }
    }

    return (
        <div className="individualPollContainer">
            <div className="tagsForPoll">
                {poll.tags.map((tag) => (
                    <span key={tag.id} className="individualTagForPoll">
                        {`t/${tag.name}`}
                    </span>
                ))}
            </div>
            <h3>{poll.question}</h3>
            <div className="optionContainer">
                {poll.options.map((option) => (
                    <IndividualOptionComponent
                        key={option.id}
                        option={option}
                        toShowResults={toShowResults}
                        castVote={castVote}
                        voteCount={option.voteCount}
                        percentOfTotalVotes={
                            totalVotes === 0
                                ? 0
                                : (option.voteCount / totalVotes) * 100
                        }
                        isThisTheOptionUserVotedFor={
                            poll.optionIdVotedFor === option.id
                        }
                    />
                ))}
            </div>
        </div>
    );
};

export default IndividualPollComponent;
