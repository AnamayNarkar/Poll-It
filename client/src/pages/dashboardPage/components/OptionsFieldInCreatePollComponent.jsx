import React from 'react'
import '../styles/OptionsFieldInCreatePollComponentStyles.css'

const OptionsFieldInCreatePollComponent = ({ id }) => {
    return (
        <input type='text' placeholder={`Option ${id + 1}`} className='optionsFieldInCreatePollComponent' />
    )
}

export default OptionsFieldInCreatePollComponent
