import {Container, Grid, Icon, Typography} from "@mui/material";
import Logo from "../../assets/logo.svg";
import {useTranslation} from "react-i18next";

const NotFoundPage = () => {
    const {t, i18n} = useTranslation();

    return (
        <div>
            <div className="landing-page-root">
                <Icon sx={{width: '40%', height: '40%', marginLeft: '1vh', marginRight: '1vh'}}>
                    <img src={Logo}/>
                </Icon>
                <Container maxWidth="sm">
                    <Grid container direction="column" alignItems="center" spacing={4}>
                        <Grid item>
                            <Typography variant="h4" component="h1">
                                {t('page_not_found.title')}
                            </Typography>
                        </Grid>
                    </Grid>
                </Container>
            </div>
        </div>
    );
}
export default NotFoundPage;