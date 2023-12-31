import {useTranslation} from "react-i18next";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {API_URL, MANAGER} from "../../consts";
import {Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import RefreshIcon from "../icons/RefreshIcon";
import {AdvanceForMonthInYear} from "../../types/AdvanceForMonthInYear";
import axios from "axios";

const AdvancesList = () => {
    const location = useLocation();
    const params = useParams();
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [advances, setAdvances] = useState<AdvanceForMonthInYear[]>([]);
    const role = localStorage.getItem("role");
    const placeId = params.placeId;
    const year = params.year;
    const URL = role === MANAGER ? `${API_URL}/balances/${placeId}/advances-values/${year}` : `${API_URL}/balances/self/${placeId}/advances-values/${year}`

    const fetchData = async () => {
        axios.get(URL, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setAdvances(response.data);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, [URL]);

    const handleClick = () => {
        fetchData();
        navigate(location.pathname);
    };

    return (
        <TableContainer component={Paper} sx={{display: 'flex', flexDirection: 'column', overflowY: 'auto'}}>
            <Table aria-label='simple table'>
                <TableHead>
                    <TableRow>
                        <TableCell>{t('annual_balance.date')}</TableCell>
                        <TableCell>
                            {t('annual_balance.water') + ' [PLN]'}
                        </TableCell>
                        <TableCell>
                            {t('annual_balance.place_heat') + ' [PLN]'}
                        </TableCell>
                        <TableCell>
                            {t('annual_balance.communal_area_heat') + ' [PLN]'}
                        </TableCell>
                        <TableCell>
                            <Button className="landing-page-button"
                                    onClick={handleClick}>
                                <RefreshIcon/>
                            </Button></TableCell>
                    </TableRow>
                </TableHead>
                <TableBody sx={{flexGrow: '1', minHeight: '0', overflowY: 'scroll'}}>
                    {advances.map((advances) => (
                        <TableRow key={advances.year + '-' + advances.month}>
                            <TableCell>{advances.year + '-' + advances.month}</TableCell>
                            <TableCell>{advances.hotWaterAdvanceValue}</TableCell>
                            <TableCell>{advances.heatingPlaceAdvanceValue}</TableCell>
                            <TableCell>{advances.heatingCommunalAreaAdvanceValue}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}
export default AdvancesList;