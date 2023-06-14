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
    TableRow
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
import path from "path/posix";
import {UnitWarmCostReport} from "../../types/unitWarmCostReport";

const NavbarPanel = () => {
    const location2 = useLocation();
    const getLocation = location2.pathname;
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

    const GlobeIcon = ({width = 24, height = 24}) => (
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width={width}
            height={height}
            fill="currentColor"
            className="bi bi-globe"
            viewBox="0 0 16 16"
        >
            <path
                d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm7.5-6.923c-.67.204-1.335.82-1.887 1.855A7.97 7.97 0 0 0 5.145 4H7.5V1.077zM4.09 4a9.267 9.267 0 0 1 .64-1.539 6.7 6.7 0 0 1 .597-.933A7.025 7.025 0 0 0 2.255 4H4.09zm-.582 3.5c.03-.877.138-1.718.312-2.5H1.674a6.958 6.958 0 0 0-.656 2.5h2.49zM4.847 5a12.5 12.5 0 0 0-.338 2.5H7.5V5H4.847zM8.5 5v2.5h2.99a12.495 12.495 0 0 0-.337-2.5H8.5zM4.51 8.5a12.5 12.5 0 0 0 .337 2.5H7.5V8.5H4.51zm3.99 0V11h2.653c.187-.765.306-1.608.338-2.5H8.5zM5.145 12c.138.386.295.744.468 1.068.552 1.035 1.218 1.65 1.887 1.855V12H5.145zm.182 2.472a6.696 6.696 0 0 1-.597-.933A9.268 9.268 0 0 1 4.09 12H2.255a7.024 7.024 0 0 0 3.072 2.472zM3.82 11a13.652 13.652 0 0 1-.312-2.5h-2.49c.062.89.291 1.733.656 2.5H3.82zm6.853 3.472A7.024 7.024 0 0 0 13.745 12H11.91a9.27 9.27 0 0 1-.64 1.539 6.688 6.688 0 0 1-.597.933zM8.5 12v2.923c.67-.204 1.335-.82 1.887-1.855.173-.324.33-.682.468-1.068H8.5zm3.68-1h2.146c.365-.767.594-1.61.656-2.5h-2.49a13.65 13.65 0 0 1-.312 2.5zm2.802-3.5a6.959 6.959 0 0 0-.656-2.5H12.18c.174.782.282 1.623.312 2.5h2.49zM11.27 2.461c.247.464.462.98.64 1.539h1.835a7.024 7.024 0 0 0-3.072-2.472c.218.284.418.598.597.933zM10.855 4a7.966 7.966 0 0 0-.468-1.068C9.835 1.897 9.17 1.282 8.5 1.077V4h2.355z"/>
        </svg>
    )
    const UserInfoIcon = ({width = 24, height = 24}) => (
        <svg xmlns="http://www.w3.org/2000/svg"
             width={width}
             height={height}
             fill="currentColor"
             className="bi bi-person-lines-fill" viewBox="0 0 16 16">
            <path
                d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm-5 6s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5zm.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1h-4zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2z"/>
        </svg>
    )

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

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
        }
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
                <Typography variant="h6" sx={{
                    marginRight: '1vh',
                    marginLeft: 'auto'
                }}><Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="#ffffff" href="/">
                        *
                    </Link>
                    {location2.pathname.includes("accounts") && <Link
                        underline="hover"
                        color="#ffffff"
                        href="/accounts/"
                    >Accounts</Link>}
                    {(location2.pathname.includes("accounts/") && !location2.pathname.includes("accounts/self")) &&
                        <Link
                            underline="hover"
                            color="#ffffff"
                            href={"/accounts/" + username}
                        >Account</Link>}
                    {location2.pathname.includes("accounts/self") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/accounts/self/" + currentRole?.toLowerCase()}
                    >Self</Link>}
                </Breadcrumbs></Typography>
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
                <Dialog disableEscapeKeyDown open={errorOpen}>
                    <DialogTitle>{t(errorOpenMessage)}</DialogTitle>
                    <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
                </Dialog>

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