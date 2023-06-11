import React, {useEffect, useState} from 'react';
import {Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from '@mui/material';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {BalancesFromList} from "../../types/BalancesFromList";
import RefreshIcon from "../icons/RefreshIcon";

const AnnualBalances = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [reports, setReports] = useState<BalancesFromList[]>([]);

    const fetchData = async () => {
        axios.get(`${API_URL}/balances/self/all-reports`, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setReports(response.data);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleClick = () => {
        fetchData();
        navigate('/annual-reports/self');
    };

    return (
        <TableContainer component={Paper}>
            <Table aria-label='simple table'>
                <TableHead>
                    <TableRow>
                        <TableCell/>
                        <TableCell>
                            {t('balances.personal_data')}
                        </TableCell>
                        <TableCell>
                            {t('balances.year')}
                        </TableCell>
                        <TableCell>
                            {t('balances.street')}
                        </TableCell>
                        <TableCell>
                            {t('balances.building_number')}
                        </TableCell>
                        <TableCell>
                            {t('balances.place_number')}
                        </TableCell>
                        <TableCell>
                            {t('balances.city')}
                        </TableCell>
                        <TableCell>
                            {t('balances.postal_code')}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {reports.map((reports) => (
                        <TableRow key={reports.id}>
                            <TableCell component='th' scope='row'/>
                            <TableCell>{reports.firstName} {reports.surname}</TableCell>
                            <TableCell>{reports.year}</TableCell>
                            <TableCell>{reports.street} </TableCell>
                            <TableCell>{reports.streetNumber}</TableCell>
                            <TableCell>{reports.placeNumber}</TableCell>
                            <TableCell>{reports.city}</TableCell>
                            <TableCell>{reports.postalCode}</TableCell>
                        </TableRow>
                    ))}
                    <TableRow><Button className="landing-page-button" onClick={handleClick}><RefreshIcon/></Button></TableRow>
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default AnnualBalances;