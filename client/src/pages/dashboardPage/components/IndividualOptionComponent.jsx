import React from 'react'
import '../styles/IndividualOptionComponentStyles.css'

const IndividualOptionComponent = ({ option }) => {
    return (
        <div className='individualOptionContainer'>
            <h4>{option.option}</h4>
            <div className='optionStats'></div>
        </div>
    )
}

export default IndividualOptionComponent
