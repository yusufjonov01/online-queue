import React, {Component} from 'react';
import {connect} from "dva";
import DashboardLayout from "../../components/DashboardLayout";
import ProfilForm from "../../components/ProfilForm";
@connect(({globalModel})=>({globalModel}))
class Cabinet extends Component {
    render() {
        const {currentUser,isAdmin,isModer,isReception,isOperator}=this.props.globalModel;
        console.log(currentUser);
        console.log(isAdmin);
        console.log(isModer);
        return (
            <div>

              {isAdmin||isModer||isReception||isOperator?<DashboardLayout pathname={this.props.pathname}>
                  <h6 className="m-5 ml-0 font-weight-normal"> Assalomu Aleylkum!
                    <b> {currentUser.firstName}</b>
                    <br/>
                    Boshqaruv paneliga xush kelibsiz!</h6>
                </DashboardLayout>:
                ""
                }

            </div>
        );
    }
}

Cabinet.propTypes = {};

export default Cabinet;
