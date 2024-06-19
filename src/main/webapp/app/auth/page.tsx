"use client"

import {useState} from "react";
import {useRouter} from "next/navigation";
import axios from "axios";

const Auth = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const router = useRouter();

    const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        // try {
        //     const response = await axios.post("http://localhost:8080/api/user/login", {
        //         email,
        //         password,
        //     }, {
        //         withCredentials: true
        //     });
        //     await router.push("/");
        // } catch (error: any) {
        //     console.error(error);
        // }

        const params = new URLSearchParams();
        params.append('email', email);
        params.append('password', password);

        try {
            const response = await axios.post(
                "http://localhost:8080/api/user/login",
                params,
                {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    withCredentials: true
                }
            );
            await router.push("/");
        } catch (error) {
            console.error(error);
        }
    };

    return(
        <main className={"flex items-center justify-center h-screen"}>
            <section className={"flex flex-col items-center w-[500px] h-[688px] border-[2px] border-black rounded-lg p-14"}>
                <div className={"flex items-center justify-center bg-[#F69F1D] w-[214px] h-[64px] rounded-lg"}>
                    <h1 className={"text-white font-bold text-lg"}>Pay My Buddy</h1>
                </div>
                <form className={"flex flex-col items-center mt-32 w-full"} onSubmit={handleLogin}>
                    <input
                        className={"w-[295px] h-[60px] rounded-lg p-6 text-lg font-bold outline-none border-[1px] border-gray-300"}
                        type={"email"}
                        placeholder={"Mail"}
                        name={"email"}
                        required={true}
                        value={email}
                        onChange={(e => setEmail(e.target.value))}
                    />
                    <input
                        className={"w-[295px] h-[60px] rounded-lg p-6 text-lg font-bold outline-none border-[1px] border-gray-300 mt-10"}
                        type={"password"}
                        placeholder={"Mot de passe"}
                        name={"password"}
                        required={true}
                        value={password}
                        onChange={(e => setPassword(e.target.value))}
                    />
                    <button className={"w-[205px] h-[64px] bg-[#207FEE] rounded-lg text-white text-lg font-bold mt-32"} type={"submit"}>Se conneter</button>
                </form>
            </section>
        </main>
    )
}

export default Auth;
