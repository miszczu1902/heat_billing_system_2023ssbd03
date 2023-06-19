import React, {useEffect, useState} from 'react';
import {Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from '@mui/material';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import RefreshIcon from "../icons/RefreshIcon";
import {HeatDistributionCenterPayoffsFromList} from "../../types/HeatDistributionCenterPayoffsFromList";
import AddInvoiceValues from "./AddInvoiceValues";

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
            if (error.response.status == 403) navigate("/");
        })
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleClick = () => {
        fetchData();
        navigate('/manage');
    };

    return (
        <div style={{overflow: 'hidden'}}>
            <TableContainer component={Paper} sx={{display: 'flex', justifyContent: 'center', maxHeight: '85vh'}}>
                <Table aria-label='simple table'>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                <Button className="landing-page-button" onClick={handleClick}>
                                    <RefreshIcon/>
                                </Button>
                            </TableCell>
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
                            <TableCell><AddInvoiceValues/></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody sx={{flexGrow: '1', minHeight: '0', overflowY: 'scroll'}}>
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
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}

export default HeatDistributionCentrePayoff;