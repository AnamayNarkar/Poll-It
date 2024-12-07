import React from 'react'
import '../styles/IndividualPollComponentStyles.css'
import IndividualOptionComponent from './IndividualOptionComponent'

const IndividualPollComponent = ({ poll }) => {
    return (
        <div className='individualPollContainer'>
            <div className='tagsForPoll'>
                {poll.tags.map((tag) => {
                    return (
                        <span key={tag.id} className='individualTagForPoll'>
                            {`t/${tag.name}`}
                        </span>
                    )
                })}
            </div>
            <h3>
                {poll.question}
            </h3>
            <div className='optionContainer'>
                {poll.options.map((option) => {
                    <IndividualOptionComponent key={option.id} option={option} />
                })}
            </div>

        </div>
    )
}


export default IndividualPollComponent
