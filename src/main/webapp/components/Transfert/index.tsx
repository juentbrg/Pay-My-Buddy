"use client"

import React, {useState, useEffect} from "react";
import Spinner from "@/components/Spinner";
import axios from "axios";

const Transfert = () => {
    const [relation, setRelation] = useState("");
    const [description, setDescription] = useState("");
    const [amount, setAmount] = useState(0);
    const [connections, setConnections] = useState([""]);

    const handleGetRelationsUsernames = async () => {
        const response = await axios.get("http://localhost:8080/api/connection/get-connection-name", {withCredentials: true})

        if (response.status === 200) {
            setConnections(response.data)
        }
    }

    const handleCreateTransaction = async (e:React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const response = await axios.post("http://localhost:8080/api/transaction/create", {
            relationName:relation,
            description,
            amount
        }, {
            withCredentials: true
        })
    }

    useEffect(() => {
        handleGetRelationsUsernames();
    }, []);

    return(
            <section className={"flex justify-center mt-20"}>
                <form className={"flex flex-row items-center"} onSubmit={handleCreateTransaction}>
                    <select
                        className={"w-[399px] h-[81px] border-[1px] border-[#E0E0E0] rounded-lg outline-none p-4 bg-white"}
                        onChange={(e => setRelation(e.target.value))}
                    >
                        <option value="" disabled selected>Sélectionner une relation</option>
                        {connections.map((connection, index) => (
                            <option key={index} value={connection}>
                                {connection}
                            </option>
                        ))}
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
