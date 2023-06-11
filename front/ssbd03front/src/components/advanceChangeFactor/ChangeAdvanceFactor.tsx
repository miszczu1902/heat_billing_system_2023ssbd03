import {useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {API_URL, MANAGER} from "../../consts";
import axios from "axios";
import {ActualAdvanceChangeFactor} from "../../types/ActualAdvanceChangeFactor";
import {Box, Paper, Typography} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import IconViewList from "../icons/IconViewList";

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

    return (<div style={{width: '50vw', boxSizing: 'border-box', left: 0, bottom: 0}}>
        <Box sx={{width: '100%', maxWidth: '600px', margin: '2vh'}}>
            <Paper elevation={3} style={{padding: '2vh'}}>
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="close"
                >
                    <IconViewList/>
                </IconButton>
                <Typography variant="h5">
                    <b>{t('annual_balance.change_factor')}: </b> {advanceChangeFactor?.advanceChangeFactor}
                </Typography>
            </Paper>
    </Box></div>)
}
export default ChangeAdvanceFactor;