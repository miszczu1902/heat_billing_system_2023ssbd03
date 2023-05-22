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
import {ButtonGroup, Icon} from '@mui/material';
import {useCookies} from 'react-cookie';
import jwt from "jwt-decode";
import {useNavigate} from "react-router-dom";
import Logo from "../../assets/logo.svg";
import "../../i18n";
import {useTranslation} from "react-i18next";
import {API_URL, ADMIN, MANAGER, OWNER, GUEST} from "../../consts";
import axios from "axios";
import UserInfoIcon from '../icons/UserInfoIcon';
import GlobeIcon from '../icons/GlobeIcon';
import SwitchUserIcon from "../icons/SwitchUserIcon";

const NavbarPanel = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [openRole, setOpenRole] = useState(false);
    const [navbarColor, setNavbarColor] = useState('#ffffff');
    const [cookies, setCookie, removeCookie] = useCookies(["token", "language", "role"]);
    const [roles, setRoles] = useState([GUEST]);
    const [currentRole, setCurrentRole] = useState(cookies.role);
    const [username, setUsername] = useState('');
    const token = "Bearer " + cookies.token;

    useEffect(() => {
        if (cookies.token !== "undefined" && cookies.token !== undefined) {
            const decodedToken = jwt(cookies.token) as string;
            const decodedRole = JSON.parse(JSON.stringify(decodedToken)).role;
            const roles = decodedRole.split(',').sort();
            const currentTimestamp = Math.floor(new Date().getTime() / 1000);
            if (JSON.parse(JSON.stringify(decodedToken)).exp < currentTimestamp) {
                removeCookie('token');
                navigate('/');
            }
            setRoles(roles);
            if (currentRole === GUEST) {
                setCurrentRole(roles[0]);
                setCookie("role", roles[0]);
            }
            setUsername(decodedToken.sub);
        } else {
            setRoles([GUEST]);
            setCurrentRole(GUEST);
        }
    }, [cookies.token]);

    const handleChange = (event: SelectChangeEvent) => {
        localStorage.setItem("selectedLanguage", event.target.value);
    };

    const handleClickOpen = () => {
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
        const languageDTO = {
            language: language
        }
        if (language) {
            i18n.changeLanguage(language.toLowerCase());
            if (cookies.token !== undefined) {
                axios.patch(`${API_URL}/accounts/self/language`,
                    languageDTO, {
                        headers: {
                            'Authorization': token,
                            'Content-Type': 'application/json'
                        },
                    })
            }
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
        setCookie("role", localStorage.getItem("selectedRole"));
        window.location.reload();
    };

    const handleClickOpenLogout = () => {
        removeCookie('token');
        setCookie("role", GUEST);
        navigate('/');
        window.location.reload();
    };

    useEffect(() => {
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
    }, [currentRole]);

    return (
        <AppBar position="static" style={{backgroundColor: navbarColor}}>
            <Toolbar>
                <Icon sx={{width: '3%', height: '3%', marginLeft: '1vh', marginRight: '1vh', cursor: 'pointer'}}>
                    <img src={Logo} alt="Logo" onClick={() => navigate('/')}/>
                </Icon>
                {
                    (currentRole === ADMIN || currentRole === MANAGER) &&
                    <Typography variant="h6" onClick={() => navigate('/accounts')} sx={{marginLeft: '1vh', cursor: 'pointer'}}>
                        {t('navbar.account_list')}
                    </Typography>
                }

                <Typography variant="h6" sx={{marginRight: '1vh', marginLeft: 'auto'}}>{t('navbar.logged_as') + username}</Typography>
                <ButtonGroup variant="contained" aria-label="outlined primary button group" sx={{marginRight: '1vh'}}>
                    <Button onClick={handleClickOpen} style={{backgroundColor: navbarColor}}><GlobeIcon/></Button>
                    {cookies.token && (
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
        </AppBar>
    );
};

export default NavbarPanel;