import React, {useEffect, useState} from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, {SelectChangeEvent} from '@mui/material/Select';
import Box from '@mui/material/Box';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import OutlinedInput from '@mui/material/OutlinedInput';
import {
    Breadcrumbs,
    ButtonGroup,
    Icon,
    Link,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Snackbar,
    Alert
} from '@mui/material';
import {useLocation, useNavigate} from "react-router-dom";
import Logo from "../../assets/logo.svg";
import "../../i18n";
import {useTranslation} from "react-i18next";
import {ADMIN, API_URL, GUEST, MANAGER, OWNER} from "../../consts";
import axios from "axios";
import UserInfoIcon from '../icons/UserInfoIcon';
import GlobeIcon from '../icons/GlobeIcon';
import SwitchUserIcon from "../icons/SwitchUserIcon";
import decode from 'jwt-decode';
import {UnitWarmCostReport} from "../../types/unitWarmCostReport";
import BreadCrumb from './BreadCrumb';

const NavbarPanel = () => {
    const [windowOpen, setWindowOpen] = useState(false);
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [openRole, setOpenRole] = useState(false);
    const [navbarColor, setNavbarColor] = useState('#ffffff');
    const [version, setVersion] = useState("");
    const [roles, setRoles] = useState([GUEST]);
    const [currentRole, setCurrentRole] = useState(localStorage.getItem("role"));
    const [username, setUsername] = useState('');
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = useState("");
    const token = "Bearer " + localStorage.getItem("token");
    const [report, setReport] = useState<UnitWarmCostReport | null>(null);
    const [openReport, setOpenReport] = useState(false);
    const [reportError, setReportError] = useState(false);

    useEffect(() => {
        if (localStorage.getItem("token") != null) {
            const dataToken = decode(token);
            const currentTimestamp = Math.floor(new Date().getTime() / 1000);
            if (JSON.parse(JSON.stringify(dataToken)).exp < currentTimestamp) {
                setWindowOpen(true);
            }
            let data = JSON.stringify({
                "token": localStorage.getItem("token"),
            });
            let config = {
                method: 'post',
                maxBodyLength: Infinity,
                url: API_URL + '/accounts/self/refresh-token',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
                data: data
            };
            axios.request(config)
                .then((response) => {
                    localStorage.setItem("token", response.headers["bearer"]);
                })
                .catch((error) => {
                    setWindowOpen(true);
                });
        }
    });

    useEffect(() => {
        if (localStorage.getItem("token") != null) {
            const data = decode(token);
            const decodedRole = JSON.parse(JSON.stringify(data)).role;
            const roles = decodedRole.split(',').sort();
            setRoles(roles);
            if (currentRole === GUEST) {
                setCurrentRole(roles[0]);
                localStorage.setItem("role", roles[0]);
            }
            setUsername(JSON.parse(JSON.stringify(data)).sub);
        } else {
            setRoles([GUEST]);
            setCurrentRole(GUEST);
        }

        switch (currentRole) {
            case ADMIN :
                setNavbarColor('#58d1fa');
                break;
            case MANAGER :
                setNavbarColor('#1c75ec');
                break;
            case OWNER :
                setNavbarColor('#7b79d4');
                break;
            default :
                setNavbarColor('#1c8de4');
                break;
        }
    }, [localStorage.getItem("token"), currentRole]);

    const handleChange = (event: SelectChangeEvent) => {
        localStorage.setItem("selectedLanguage", event.target.value);
    };

    const handleClickOpen = () => {
        fetchData();
        setOpen(true);
    };

    const handleCancel = () => {
        setOpen(false);
        setOpenRole(false);
    };

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
        const language = localStorage.getItem("selectedLanguage");
        if (language) {
            i18n.changeLanguage(language.toLowerCase());
            if (localStorage.getItem("token") !== null) {
                const languageDTO = {
                    version: parseInt(version),
                    language: language
                }
                axios.patch(`${API_URL}/accounts/self/language`,
                    languageDTO, {
                        headers: {
                            'Authorization': token,
                            'If-Match': localStorage.getItem("etag"),
                            'Content-Type': 'application/json'
                        },
                    })
                    .catch(error => {
                        setErrorOpenMessage(t('navbar.languages.error'))
                        setErrorOpen(true);
                    });
            }
        }
    };

    const fetchData = async () => {
        if (localStorage.getItem("token") !== null) {
            await axios.get(`${API_URL}/accounts/self`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    localStorage.setItem("etag", response.headers.etag);
                    setVersion(response.data.version.toString());
                });
        }
    };

    const handleOpenRole = () => {
        setOpenRole(true);
    }

    const handleChangeRole = (event: SelectChangeEvent) => {
        localStorage.setItem("selectedRole", event.target.value);
    };

    const handleCloseRole = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpenRole(false);
        }
        const selectedRole = localStorage.getItem("selectedRole");
        if (selectedRole) {
            localStorage.setItem("role", selectedRole);
            window.location.reload();
        }
    };

    const handleClickOpenLogout = () => {

        navigate("/logout");
    };

    const handleConfirm = () => {
        localStorage.removeItem("token");
        navigate("/login");
    };

    const handleErrorClose = () => {
        setErrorOpen(false);
    };

    const handleReportClick = () => {
        if (currentRole === GUEST) {
            axios.get(`${API_URL}/balances/unit-cost-report`, {})
                .then(response => {
                    setReport(response.data);
                    setOpenReport(true);
                }).catch(error => {
                setReportError(true);
            });
        } else {
            axios.get(`${API_URL}/balances/unit-cost-report`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    setReport(response.data);
                    setOpenReport(true);
                }).catch(error => {
                setReportError(true);
            });
        }
    };

    return (
        <div>
            <Dialog disableEscapeKeyDown open={windowOpen}>
                <DialogTitle>{t('token.token_not_valid_message')}</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirm}>OK</Button>
                </DialogActions>
            </Dialog>

            <AppBar position="static" style={{backgroundColor: navbarColor}}>
                <Toolbar>
                    <Icon sx={{width: '3%', height: '3%', marginLeft: '1vh', marginRight: '1vh', cursor: 'pointer'}}>
                        <img src={Logo} alt="Logo" onClick={() => navigate('/')}/>
                    </Icon>
                    <ButtonGroup variant="contained" aria-label="outlined primary button group">
                        {
                            (currentRole === ADMIN || currentRole === MANAGER) &&
                            <Button style={{backgroundColor: navbarColor}} onClick={() => navigate('/accounts')}
                                    sx={{marginLeft: '1vh', cursor: 'pointer'}}>
                                {t('navbar.account_list')}
                            </Button>
                        }
                        {
                            (currentRole === MANAGER) &&
                            <Button style={{backgroundColor: navbarColor}} onClick={() => navigate('/buildings')}
                                    sx={{marginLeft: '1vh', cursor: 'pointer'}}>
                                {t('navbar.building_list')}
                            </Button>
                        }
                        {
                            (currentRole === OWNER) &&
                            <Button style={{backgroundColor: navbarColor}} onClick={() => navigate('/places/self')}
                                    sx={{marginLeft: '1vh', cursor: 'pointer'}}>
                                {t('navbar.places_list')}
                            </Button>
                        }
                        {
                            (currentRole === OWNER) &&
                            <Button style={{backgroundColor: navbarColor}}
                                    onClick={() => navigate('/annual-reports/self')}
                                    sx={{marginLeft: '1vh', cursor: 'pointer'}}>
                                {t('navbar.annual_reports')}
                            </Button>
                        }
                        {
                            (currentRole === MANAGER) &&
                            <Button style={{backgroundColor: navbarColor}} onClick={() => navigate('/manage')}
                                    sx={{marginLeft: '1vh', cursor: 'pointer'}}>
                                {t('navbar.manage')}
                            </Button>
                        }
                        {
                            (currentRole === MANAGER || currentRole === GUEST || currentRole === OWNER) &&
                            <Button
                                style={{backgroundColor: navbarColor}}
                                onClick={handleReportClick}
                                sx={{marginLeft: '1vh', cursor: 'pointer'}}
                            >
                                {t('navbar.report')}
                            </Button>
                        }
                    </ButtonGroup>
                    <Typography variant="subtitle1" sx={{
                        marginRight: '1vh',
                        marginLeft: 'auto'
                    }}>{localStorage.getItem("token") && t('navbar.logged_as') + username}</Typography>
                    <ButtonGroup variant="contained" aria-label="outlined primary button group"
                                 sx={{marginRight: '1vh'}}>
                        <Button onClick={handleClickOpen} style={{backgroundColor: navbarColor}}><GlobeIcon/></Button>
                        {localStorage.getItem("token") && (
                            <>
                                {
                                    (currentRole === OWNER)
                                    && <Button style={{backgroundColor: navbarColor}}
                                               onClick={() => navigate('/accounts/self/owner')}><UserInfoIcon/></Button>
                                }
                                {
                                    (currentRole === MANAGER)
                                    && <Button style={{backgroundColor: navbarColor}}
                                               onClick={() => navigate('/accounts/self/manager')}><UserInfoIcon/></Button>
                                }
                                {
                                    (currentRole === ADMIN)
                                    && <Button style={{backgroundColor: navbarColor}}
                                               onClick={() => navigate('/accounts/self/admin')}><UserInfoIcon/></Button>
                                }
                                <Button onClick={handleOpenRole}
                                        style={{backgroundColor: navbarColor}}><SwitchUserIcon/></Button>
                                <Button onClick={handleClickOpenLogout}
                                        style={{backgroundColor: navbarColor}}>{t('navbar.log_out')}</Button>
                            </>
                        )}
                    </ButtonGroup>
                </Toolbar>
                <BreadCrumb/>
                <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                    <DialogTitle>{t('navbar.languages.title')}</DialogTitle>
                    <DialogContent>
                        <Box component="form" sx={{display: 'flex', flexWrap: 'wrap'}}>
                            <FormControl sx={{m: 1, minWidth: 120}}>
                                <InputLabel id="demo-dialog-select-label">{t('navbar.languages.default')}</InputLabel>
                                <Select
                                    labelId="demo-dialog-select-label"
                                    id="demo-dialog-select"
                                    onChange={handleChange}
                                    input={<OutlinedInput label={t('navbar.languages.default')}/>}
                                >
                                    <MenuItem value={'PL'}>{t('navbar.languages.pl')}</MenuItem>
                                    <MenuItem value={'EN'}>{t('navbar.languages.en')}</MenuItem>
                                </Select>
                            </FormControl>
                        </Box>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCancel}>{t('confirm.cancel')}</Button>
                        <Button onClick={handleClose}>{t('confirm.ok')}</Button>
                    </DialogActions>
                </Dialog>
                <Dialog disableEscapeKeyDown open={openRole} onClose={handleCloseRole}>
                    <DialogTitle>{t('navbar.select_role')}</DialogTitle>
                    <DialogContent>
                        <Box component="form" sx={{display: 'flex', flexWrap: 'wrap'}}>
                            <FormControl sx={{m: 1, minWidth: 200}}>
                                <InputLabel
                                    id="demo-dialog-select-label">{t('navbar.roles')}</InputLabel>
                                <Select
                                    labelId="demo-dialog-select-label"
                                    id="demo-dialog-select"
                                    onChange={handleChangeRole}
                                    input={<OutlinedInput label={t('navbar.languages.default')}/>}>
                                    {roles.map((roleToDisplay) => (
                                        <MenuItem
                                            value={roleToDisplay}>{t('profile.' + roleToDisplay.toLowerCase())}</MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                        </Box>
                    </DialogContent>
                    <DialogActions sx={{alignItems: 'center', justifyContent: 'center'}}>
                        <Button onClick={handleCloseRole}>{t('confirm.ok')}</Button>
                    </DialogActions>
                </Dialog>

                <Snackbar open={errorOpen} autoHideDuration={6000} onClose={handleErrorClose}>
                    <Alert onClose={handleErrorClose} severity="error" sx={{width: '100%'}}>
                        {t(errorOpenMessage)}
                    </Alert>
                </Snackbar>

                <Dialog open={openReport}>
                    <DialogTitle style={{textAlign: "center"}}>{t('navbar.costs_info')}</DialogTitle>
                    <DialogContent>
                        <TableContainer>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>{t('navbar.price_per_cubic_meter')}</TableCell>
                                        <TableCell>{t('navbar.price_per_square_meter')}</TableCell>
                                        <TableCell>{t('navbar.month_year')}</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    <TableRow>
                                        <TableCell>{report?.pricePerCubicMeter} PLN</TableCell>
                                        <TableCell>{report?.pricePerSquareMeter} PLN</TableCell>
                                        <TableCell>{`${report?.month}.${report?.year}`}</TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </DialogContent>
                    <DialogActions style={{justifyContent: "center"}}>
                        <Button onClick={() => setOpenReport(false)} color="primary">
                            {t('navbar.close')}
                        </Button>
                    </DialogActions>
                </Dialog>
                <Dialog disableEscapeKeyDown open={reportError}>
                    <DialogTitle>{t('navbar.costs_error')}</DialogTitle>
                    <Button onClick={() => setReportError(false)} color="primary">
                        {t('navbar.close')}
                    </Button>
                </Dialog>
            </AppBar>
        </div>
    );
};

export default NavbarPanel;