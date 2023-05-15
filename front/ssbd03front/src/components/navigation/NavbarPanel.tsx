import React, {useEffect} from 'react';
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

const NavbarPanel: React.FC = () => {
    const navigate = useNavigate();
    const [open, setOpen] = React.useState(false);
    const [language, setLanguage] = React.useState<string>('');
    const [navbarColor, setNavbarColor] = React.useState('#ffffff');
    const [cookies, setCookie, removeCookie] = useCookies(["token", "language"]);
    const [role, setRole] = React.useState('GUEST');

    useEffect(() => {
        if (cookies.token != "undefined" && cookies.token != undefined) {
            const decodedToken = jwt(cookies.token);
            setRole(JSON.parse(JSON.stringify(decodedToken)).role);
            const currentTimestamp = Math.floor(new Date().getTime() / 1000);
            if (JSON.parse(JSON.stringify(decodedToken)).exp < currentTimestamp) {
                removeCookie('token');
                navigate('');
            }
        } else {
            setRole('GUEST');
        }
    }, [cookies.token, role]);

    const handleChange = (event: SelectChangeEvent<typeof language>) => {
        setLanguage(event.target.value);
    };

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    };
    const handleClickOpenLogin = () => {
        navigate('/login');
    };
    const handleClickOpenLogout = () => {
        navigate('/');
        window.location.reload();
        removeCookie('token');
        setTimeout(() => {
        }, 6000);
    };

    useEffect(() => {
        switch (role) {
            case 'ADMIN':
                setNavbarColor('#58d1fa');
                break;
            case 'MANAGER':
                setNavbarColor('#1c75ec');
                break;
            case 'OWNER':
                setNavbarColor('#7b79d4');
                break;
            case 'GUEST':
                setNavbarColor('#1c8de4');
        }
    }, [role]);

    return (

        <AppBar position="static" style={{backgroundColor: navbarColor}}>
            <Toolbar>
                <Icon sx={{width: '3%', height: '3%', marginLeft: '1vh', marginRight: '1vh'}}>
                    <img src={Logo} alt="Logo" onClick={() => navigate('/')}/>
                </Icon>
                {
                    (role === 'ADMIN' || role === 'MANAGER') &&
                    <Typography variant="h6" sx={{flexGrow: 1, marginLeft: 2}} onClick={() => navigate('/accounts')}>
                        Lista kont
                    </Typography>
                }

                <ButtonGroup variant="contained" aria-label="outlined primary button group" sx={{marginLeft: 'auto'}}>
                    <Button style={{backgroundColor: navbarColor}}>Zmień język</Button>
                    {cookies.token && (
                        <Button onClick={handleClickOpenLogout} style={{backgroundColor: navbarColor}}>Wyloguj</Button>
                    )}
                </ButtonGroup>
            </Toolbar>
            <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                <DialogTitle>Zmień język</DialogTitle>
                <DialogContent>
                    <Box component="form" sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <FormControl sx={{m: 1, minWidth: 120}}>
                            <InputLabel id="demo-dialog-select-label">Język</InputLabel>
                            <Select
                                labelId="demo-dialog-select-label"
                                id="demo-dialog-select"
                                value={language}
                                onChange={handleChange}
                                input={<OutlinedInput label="Język"/>}
                            >
                                <MenuItem value={'PL'}>Polski</MenuItem>
                                <MenuItem value={'EN'}>Angielski</MenuItem>
                            </Select>
                        </FormControl>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleClose}>Ok</Button>
                </DialogActions>
            </Dialog>
        </AppBar>
    );
};

export default NavbarPanel;