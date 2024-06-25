"use client"

import React, {useState} from "react";
import Spinner from "@/components/Spinner";

const Transfert = () => {
    const [description, setDescription] = useState("");
    const [amount, setAmount] = useState(0);

    return(
            <section className={"flex justify-center mt-20"}>
                <form className={"flex flex-row items-center"}>
                    <select className={"w-[399px] h-[81px] border-[1px] border-[#E0E0E0] rounded-lg outline-none p-4 bg-white"}>
                        <option value="" disabled selected>Sélectionnez une relation</option>
                    </select>
                    <input
                        className={"w-[382px] h-[81px] ml-10 border-[1px] border-[#E0E0E0] rounded-lg outline-none p-4 placeholder-black"}
                        type={"text"}
                        placeholder={"Description"}
                        name={"description"}
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                    ></input>
                    <Spinner amount={amount} setAmount={setAmount}/>
                    <button className={"w-[120px] h-[88px] bg-[#207FEE] rounded-lg text-white text-lg font-bold ml-20 transition-bg duration-200 ease-in-out hover:bg-[#4192f1]"} type={"submit"}>Payer</button>
                </form>
            </section>
        )
}

export default Transfert;
