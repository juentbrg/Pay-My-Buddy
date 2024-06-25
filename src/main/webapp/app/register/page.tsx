"use client"

import {useState} from "react";
import {useRouter} from "next/navigation";
import axios from "axios";
import Link from "next/link";

const Register = () => {
    const [username, setUsername] = useState("")
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const router = useRouter();

    const handleSignIn = async(e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response = axios.post("http://localhost:8080/api/user/register", {
                username,
                email,
                password
            })
        } catch (error) {
            console.error(error)
        }
    }

    return (
        <main className={"flex items-center justify-center h-screen"}>
            <section
                className={"flex flex-col items-center w-[500px] h-[688px] border-[2px] border-black rounded-lg p-14"}>
                <div className={"flex items-center justify-center bg-[#F69F1D] w-[214px] h-[64px] rounded-lg"}>
                    <h1 className={"text-white font-bold text-lg"}>Pay My Buddy</h1>
                </div>
                <form className={"flex flex-col items-center mt-20 w-full"} onSubmit={handleSignIn}>
                    <input
                        className={"w-[295px] h-[60px] rounded-lg p-6 text-lg font-bold outline-none border-[1px] border-gray-300"}
                        type={"text"}
                        placeholder={"Nom d'utilisateur"}
                        name={"username"}
                        required={true}
                        value={username}
                        onChange={(e => setUsername(e.target.value))}
                    />
                    <input
                        className={"w-[295px] h-[60px] rounded-lg p-6 text-lg font-bold outline-none border-[1px] border-gray-300 mt-10"}
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
                    <p className={"mt-8"}>Déjà un compte ? <Link className={"text-[#207FEE] underline"} href={"/login"}>Se connecter</Link></p>
                    <button className={"w-[205px] h-[64px] bg-[#207FEE] rounded-lg text-white text-lg font-bold mt-8 transition-bg duration-200 ease-in-out hover:bg-[#4192f1]"}
                            type={"submit"}>S'inscrire
                    </button>
                </form>
            </section>
        </main>
    )
}

export default Register;
