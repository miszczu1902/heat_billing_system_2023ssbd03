import {useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import {API_URL, MANAGER} from "../../consts";
import axios from "axios";
import {ActualAdvanceChangeFactor} from "../../types/ActualAdvanceChangeFactor";

const ChangeAdvanceFactor = () => {
    const params = useParams();
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [advanceChangeFactor, setAdvanceChangeFactor] = useState<ActualAdvanceChangeFactor>();
    const role = localStorage.getItem("role");
    const buildingId = params.buildingId;
    const URL  = `${API_URL}/heat-distribution-centre/parameters/advance-change-factor/${buildingId}`

    const fetchData = async () => {
        axios.get(URL, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setAdvanceChangeFactor(response.data);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, []);

    return (<div>{advanceChangeFactor?.advanceChangeFactor}</div>)
}
export default ChangeAdvanceFactor;