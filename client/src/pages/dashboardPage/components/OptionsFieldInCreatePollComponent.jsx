import React from 'react'
import '../styles/OptionsFieldInCreatePollComponentStyles.css'
const OptionsFieldInCreatePollComponent = ({ id, value, onChange }) => {
    return (
        <input
            type='text'
            placeholder={`Option ${id + 1}`}
            className='optionsFieldInCreatePollComponent'
            value={value}
            onChange={(e) => onChange(e.target.value)}
        />
    );
};

export default OptionsFieldInCreatePollComponent;
