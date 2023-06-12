import AdvancesList from "../advances/AdvancesList";
import {Box, Paper, Typography} from "@mui/material";
import React, {useEffect, useState} from "react";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {API_URL, MANAGER} from "../../consts";
import axios from "axios";
import { YearReport } from "../../types/YearReport";

const AnnualBalance = () => {
    const params = useParams();
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [annualBalance, setAnnualBalance] = useState<YearReport>();
    const reportId = params.reportId;
    const URL = `${API_URL}/balances/report/${reportId}`;

    const fetchData = async () => {
        axios.get(URL, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setAnnualBalance(response.data);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, [URL]);

    return (
        <div style={{display: 'flex'}}>
            <Box sx={{margin: '2vh'}}>
                    <Paper elevation={3} style={{padding: '2vh'}}>
                        <Typography variant="h5">
                            <b>{t('year_report.year')}: </b> {annualBalance?.year}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.address')}: </b>
                            {annualBalance?.street + ' ' + annualBalance?.buildingNumber + ' ' + annualBalance?.city}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.postalCode')}: </b> {annualBalance?.postalCode}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.placeNumber')}: </b> {annualBalance?.placeNumber}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.totalHeatingCommunalAreaAdvance')}:</b> {annualBalance?.totalHeatingCommunalAreaAdvance + ' PLN'}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.totalHeatingCommunalAreaCost')}:  </b> {annualBalance?.totalHeatingCommunalAreaCost + ' PLN'}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.totalHeatingPlaceAdvance')}: </b> {annualBalance?.totalHeatingPlaceAdvance + ' PLN'}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.totalHeatingPlaceCost')}: </b> {annualBalance?.totalHeatingPlaceCost + ' PLN'}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.totalHotWaterAdvance')}: </b> {annualBalance?.totalHotWaterAdvance + ' PLN'}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('year_report.totalHotWaterCost')}: </b> {annualBalance?.totalHotWaterCost + ' PLN'}
                        </Typography>
                    </Paper>
            </Box>
            <Box sx={{margin: '2vh'}}><AdvancesList/></Box>
        </div>);
}
export default AnnualBalance;