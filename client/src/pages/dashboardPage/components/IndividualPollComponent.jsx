import React, { useEffect, useState } from 'react';
import '../styles/IndividualPollComponentStyles.css';
import IndividualOptionComponent from './IndividualOptionComponent.jsx';
import voteForPoll from '../../../services/ApiRequests/voteForPollRequest.js';
import { useNavigate } from 'react-router-dom'

const IndividualPollComponent = ({ poll }) => {

    const navigate = useNavigate();

    const [totalVotes, setTotalVotes] = useState(poll.totalVoteCount)
    const [toShowResults, setToShowResults] = useState(false);
    const [isVotingDisabled, setIsVotingDisabled] = useState(false);
    const [commentCount, setCommentCount] = useState(poll.totalCommentCount);

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
            <div className='pollExpiryDetailsParentDiv' style={{
                display: 'flex', flexDirection: 'row', justifyContent: 'flex-end', alignItems: 'center', backgroundColor: 'transparent', height: "25px", width: '100%', color: 'grey', fontSize: '0.8rem', fontFamily: "'Lato', sans-serif", paddingRight: '1000px'
            }}>
                <span style={{ marginRight: '30px' }}>
                    {new Date(poll.expirationDateTime) > new Date()
                        ? `Expires on ${new Date(poll.expirationDateTime).toDateString()} `
                        : 'Poll has expired'}
                </span>
            </div>
            <div className='bottomBarInPollComponent'>
                <div className="bottomBarComponent" style={{
                    color: 'white',
                    fontSize: '1.2rem',
                    fontFamily: "'Lato', sans-serif",
                    gap: '5px',
                    alignItems: 'center',
                    borderBottomLeftRadius: '10px',
                }}>
                    <svg width="30" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg" >
                        <path d="M24.9999 12.5V33.3333M24.9999 12.5V8.33329C24.9999 7.41282 24.2537 6.66663 23.3333 6.66663H16.6666C15.7461 6.66663 14.9999 7.41282 14.9999 8.33329V17.5M24.9999 12.5H33.3333C34.2537 12.5 34.9999 13.2461 34.9999 14.1666V31.6666C34.9999 32.5871 34.2537 33.3333 33.3333 33.3333H24.9999M24.9999 33.3333H14.9999M14.9999 17.5V33.3333M14.9999 17.5H6.66605C5.74558 17.5 4.99994 18.2461 4.99994 19.1666V31.6666C4.99994 32.5871 5.74613 33.3333 6.66661 33.3333H14.9999" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                    {totalVotes}
                </div>
                <div className="bottomBarComponent commentsIconParentDiv" style={{ cursor: 'pointer', gap: '5px', color: 'white', fontSize: '1.2rem', fontFamily: "'Lato', sans-serif" }} >
                    <svg width="28" height="28" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M10.3109 27.5817C10.5439 26.9445 10.2732 26.2541 9.72155 25.8592C9.46605 25.6762 9.2139 25.4901 8.96934 25.2932C7.57831 24.1837 6.4228 22.8567 5.71488 21.1699C4.56526 18.4356 4.82423 15.7963 6.3313 13.2867C7.8946 10.6837 10.2149 9.05513 12.9326 8.01855C17.4375 6.29902 21.9949 6.21393 26.5502 7.79738C29.2151 8.72339 31.5378 10.2096 33.2592 12.5805C35.7272 15.9833 35.5549 20.3337 32.8432 23.5588C30.6459 26.1716 27.804 27.591 24.6176 28.3725C22.8878 28.7973 18.4121 28.071 17.2606 29.0457C14.8348 31.1626 12.0516 32.5017 8.96004 33.1774C8.67879 33.2382 8.39051 33.277 8.10572 33.3196C7.74478 33.3743 7.44945 33.271 7.26429 32.921C7.0803 32.571 7.1471 32.2526 7.37679 31.9634C7.64398 31.628 7.91587 31.295 8.20766 30.9864C9.13189 30.0127 9.8465 28.8518 10.3109 27.5817Z" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                    {commentCount}
                </div>
                <div className="bottomBarComponent shareIconParentDiv" style={{ cursor: 'pointer', borderBottomRightRadius: '10px' }} onClick={() => { navigator.clipboard.writeText(`http://localhost:5173/p/${poll.id}`); window.alert('Link copied to clipboard!') }}>
                    < svg width="20" height="20" viewBox="0 0 77 77" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <g clip-path="url(#clip0_4_3)">
                            <path d="M52.7815 12.6736C52.7812 9.88744 53.7606 7.18983 55.5483 5.05279C57.336 2.91574 59.8182 1.47534 62.5607 0.983585C65.3031 0.491835 68.1312 0.980052 70.55 2.36282C72.9689 3.74559 74.8245 5.93485 75.7922 8.54758C76.76 11.1603 76.7782 14.0301 75.8437 16.655C74.9093 19.2798 73.0816 21.4924 70.6805 22.9058C68.2794 24.3192 65.4578 24.8433 62.7093 24.3864C59.9608 23.9296 57.4605 22.5208 55.6458 20.4066L23.7353 35.2266C24.4689 37.5515 24.4689 40.0458 23.7353 42.3706L55.6458 57.1906C57.564 54.9597 60.2422 53.5203 63.1612 53.1515C66.0802 52.7826 69.0322 53.5105 71.4452 55.1942C73.8581 56.8778 75.56 59.3972 76.2212 62.2642C76.8824 65.1312 76.4557 68.1415 75.0238 70.7119C73.592 73.2822 71.2569 75.2294 68.4712 76.1762C65.6854 77.1229 62.6474 77.0018 59.9459 75.8363C57.2443 74.6708 55.0717 72.5439 53.8489 69.8678C52.6262 67.1916 52.4405 64.1569 53.3278 61.3516L21.4173 46.5316C19.8376 48.3726 17.732 49.6856 15.3837 50.294C13.0355 50.9025 10.5572 50.7771 8.2823 49.9349C6.0074 49.0927 4.04501 47.5739 2.65911 45.583C1.27321 43.5921 0.530289 41.2244 0.530289 38.7986C0.530289 36.3728 1.27321 34.0052 2.65911 32.0143C4.04501 30.0233 6.0074 28.5046 8.2823 27.6624C10.5572 26.8201 13.0355 26.6948 15.3837 27.3032C17.732 27.9117 19.8376 29.2247 21.4173 31.0656L53.3278 16.2456C52.9646 15.0898 52.7803 13.8852 52.7815 12.6736Z" fill="#F1F1F1" />
                        </g>
                        <defs>
                            <clipPath id="clip0_4_3">
                                <rect width="76" height="76" fill="white" />
                            </clipPath>
                        </defs>
                    </svg>

                </div>
            </div>
        </div >
    );
};

export default IndividualPollComponent;
