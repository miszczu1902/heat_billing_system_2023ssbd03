import AdvancesList from "../advances/AdvancesList";
import {MANAGER} from "../../consts";

const AnnualBalance = () => {
    const role = localStorage.getItem("role");

    return (<div>Tu bedzie raport{ role === MANAGER && <AdvancesList/>}</div>);
}
export default AnnualBalance;