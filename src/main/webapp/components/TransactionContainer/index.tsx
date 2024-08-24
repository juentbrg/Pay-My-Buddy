"use client"

import Transaction from "@/components/Transaction";

interface TransactionObj {
    relationName: string
    description: string
    amount: number
}

interface TransactionContainerProps {
    transactions: TransactionObj[];
}


const TransactionContainer: React.FC<TransactionContainerProps> = ({ transactions }) => {
    return(
        <section className={"flex justify-center"}>
            <div className={"w-[1300px] h-[426px] border-[1px] border-[#E0E0E0] rounded-lg mt-20 p-4"}>
                <h3 className={"font-[600]"}>Mes Transactions</h3>
                <div className={"w-full mt-2"}>
                    <div className={"flex flex-row mb-1"}>
                        <h4 className={"w-[33.33%] font-[600]"}>Relations</h4>
                        <h4 className={"flex ml-[18%] w-[33.33%] font-[600]"}>Description</h4>
                        <h4 className={"flex justify-end w-[33.33%] font-[600]"}>Montant</h4>
                    </div>
                    {transactions && transactions.map((transaction, index) => (
                        <Transaction key={index} relation={transaction.relationName} description={transaction.description} amount={transaction.amount} />
                    ))}
                </div>
            </div>
        </section>
        )
}

export default TransactionContainer
