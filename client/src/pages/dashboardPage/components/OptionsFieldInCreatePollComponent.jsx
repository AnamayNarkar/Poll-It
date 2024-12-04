import React from 'react'
import '../styles/OptionsFieldInCreatePollComponentStyles.css'

const OptionsFieldInCreatePollComponent = ({ key }) => {
    return (
        <input type='text' placeholder={`Option ${key}`} className='optionsFieldInCreatePollComponent' key={key} />
    )
}

export default OptionsFieldInCreatePollComponent
