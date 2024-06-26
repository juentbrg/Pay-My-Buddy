"use client"

import React from "react";

interface TransactionProps {
    relation: string
    description: string
    amount: number
}

const Transaction: React.FC<TransactionProps> = ({relation, description, amount}) => {
    return (
        <div className={"flex flex-row w-full border-t-[1px] border-[#E0E0E0] pt-1 pb-1"}>
            <p className={"w-[33.33%]"}>{relation}</p>
            <p className={"flex ml-[18%] w-[33.33%]"}>{description}</p>
            <p className={"flex justify-end w-[33.33%] "}>{amount.toString() + "€"}</p>
        </div>
    )
}

export default Transaction;
