import React from 'react'
import "../styles/IndividualCommentComponentStyles.css"
import { useNavigate } from 'react-router-dom'

const IndividualCommentComponent = ({ comment }) => {
    const navigate = useNavigate();
    return (
        <div className='individualCommentComponent'>
            <img src={comment.profilePictureURL} onClick={() => { navigate(`/u/${comment.username})`) }} />
            <div className='individualCommentComponentUsernameAndComment'>
                <h5 className='commentUsername' onClick={() => { navigate(`/u/${comment.username}`) }}>{comment.username} </h5>
                <p className='commentContent'>{comment.comment}</p>
            </div>
        </div>
    )
}

export default IndividualCommentComponent
