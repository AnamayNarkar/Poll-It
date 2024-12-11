import React, { useEffect, useState } from 'react';
import '../styles/IndividualPollComponentStyles.css';
import IndividualOptionComponent from './IndividualOptionComponent.jsx';
import voteForPoll from '../../../services/ApiRequests/voteForPollRequest.js';
import { useNavigate } from 'react-router-dom'

const IndividualPollComponent = ({ poll }) => {

    const navigate = useNavigate();

    const [totalVotes, setTotalVotes] = useState(
        poll.options.reduce((acc, option) => acc + option.voteCount, 0)
    );
    const [toShowResults, setToShowResults] = useState(false);
    const [isVotingDisabled, setIsVotingDisabled] = useState(false);

    useEffect(() => {
        console.log(poll);
        if (new Date() > new Date(poll.expirationDateTime) || poll.hasUserVotedForThisPoll) {
            setToShowResults(true);
        }
    }, [poll]);

    async function castVote(option) {
        setIsVotingDisabled(true);
        try {
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
        } catch (error) {
            window.alert('Error casting vote');
        } finally {
            setIsVotingDisabled(false);
        }
    }

    return (
        <div className="individualPollContainer">
            <div className="tagsForPoll">
                <div style={{ width: 'fit-content', height: 'fit-content', display: 'flex', gap: '5px' }}>
                    {poll.tags.map((tag) => (
                        <span key={tag.id} className="individualTagForPoll" onClick={() => { navigate(`/t/${tag.name}`) }}>
                            {`t/${tag.name}`}
                        </span>
                    ))}
                </div>
                <span className='creatorOfPoll' onClick={() => { navigate(`/u/${poll.createdBy}`) }}>by u/{poll.createdBy}</span>
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
                        isVotingDisabled={isVotingDisabled}
                    />
                ))}
            </div>
        </div>
    );
};

export default IndividualPollComponent;
