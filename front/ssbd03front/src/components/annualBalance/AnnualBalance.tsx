import AdvancesList from "../advances/AdvancesList";
import ChangeAdvanceFactor from "../advanceChangeFactor/ChangeAdvanceFactor";
import {MANAGER} from "../../consts";

const AnnualBalance = () => {
    const role = localStorage.getItem("role");
    return (<div>Tu bedzie raport {role === MANAGER && <ChangeAdvanceFactor/>}<AdvancesList/></div>);
}
export default AnnualBalance;