import React, { useState } from 'react';
import Image from "next/image"
import caretUp from '../../assets/icons/caret-up.svg'
import caretDown from '../../assets/icons/caret-down.svg'

interface SpinnerProps {
    amount: number;
    setAmount: React.Dispatch<React.SetStateAction<number>>;
}

const Spinner: React.FC<SpinnerProps> = ({ amount, setAmount }) => {

    const increment = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.preventDefault();
        setAmount(prevAmount => prevAmount + 1);
    };

    const decrement = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.preventDefault();
        if (amount > 0) {
            setAmount(prevAmount => prevAmount - 1);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = parseInt(e.target.value, 10);
        if (!isNaN(value)) {
            setAmount(value);
        }
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (amount < 10) {
            if (e.key === 'Backspace' || e.key === 'Delete') {
                setAmount(0);
            }
        }
    };

    return (
        <div className={"relative ml-10 flex flex-row items-center justify-center border-[1px] border-[#E0E0E0] rounded-lg w-[189px] h-[121px] p-4"}>
            <input className="relative outline-none w-[70%] h-[50px]" type="text" value={amount} onChange={handleChange} onKeyDown={handleKeyDown}/>
            <span className={"absolute left-28"}>€</span>
            <div className={"flex flex-col"}>
                <button className="spinner-button" onClick={increment}><Image src={caretUp} alt={"caret up"}/></button>
                <button className="spinner-button" onClick={decrement}><Image src={caretDown} alt={"caret down"}/></button>
            </div>
        </div>
    );
};

export default Spinner;
