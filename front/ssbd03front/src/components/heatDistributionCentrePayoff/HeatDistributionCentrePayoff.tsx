import React, {useEffect, useState} from 'react';
import {Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from '@mui/material';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import RefreshIcon from "../icons/RefreshIcon";
import {HeatDistributionCenterPayoffsFromList} from "../../types/HeatDistributionCenterPayoffsFromList";

const HeatDistributionCentrePayoff = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const location = useLocation();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [payoffs, setPayoffs] = useState<HeatDistributionCenterPayoffsFromList[]>([]);

    const fetchData = async () => {
        axios.get(`${API_URL}/heat-distribution-centre/parameters`, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setPayoffs(response.data);
        }).catch(error => {
            if (error.response.status == 403) navigate(location.pathname);
        })
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleClick = () => {
        fetchData();
        navigate('/heat-distribution-centre');
    };

    return (
        <TableContainer component={Paper}>
            <Table aria-label='simple table'>
                <TableHead>
                    <TableRow>
                        <TableCell/>
                        <TableCell>
                            {t('heatDistributionCentrePayoff.date')}
                        </TableCell>
                        <TableCell>
                            {t('heatDistributionCentrePayoff.manager')}
                        </TableCell>
                        <TableCell>
                            {t('heatDistributionCentrePayoff.heatingAreaFactor')}
                        </TableCell>
                        <TableCell>
                            {t('heatDistributionCentrePayoff.consumption')}
                        </TableCell>
                        <TableCell>
                            {t('heatDistributionCentrePayoff.consumptionCost')}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {payoffs.map((payoffs) => (
                        <TableRow>
                            <TableCell component='th' scope='row'/>
                            <TableCell>{payoffs.date}</TableCell>
                            <TableCell>{payoffs.manager}</TableCell>
                            <TableCell>{payoffs.heatingAreaFactor} </TableCell>
                            <TableCell>{payoffs.consumption}</TableCell>
                            <TableCell>{payoffs.consumptionCost}</TableCell>
                        </TableRow>
                    ))}
                    <TableRow><Button className="landing-page-button" onClick={handleClick}><RefreshIcon/></Button></TableRow>
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default HeatDistributionCentrePayoff;