import AdvancesList from "../advances/AdvancesList";
import {MANAGER} from "../../consts";
import ChangeAdvanceFactor from "../advanceChangeFactor/ChangeAdvanceFactor";

const AnnualBalance = () => {
    const role = localStorage.getItem("role");

    return (<div>Tu bedzie raport{ role === MANAGER && <div><ChangeAdvanceFactor/><AdvancesList/></div>}</div>);
}
export default AnnualBalance;