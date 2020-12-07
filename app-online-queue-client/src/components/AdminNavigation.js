import React, {Component} from 'react';
import {
  Collapse,
  Container,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  Nav,
  Navbar,
  NavbarBrand,
  NavbarToggler,
  NavItem,
  NavLink,
  UncontrolledDropdown
} from "reactstrap";
import PropTypes from 'prop-types';
import {connect} from "dva";
import {TOKEN_NAME} from "../constants/constants";
import {router} from "umi";
// import {logo} from "../layouts/index";
import logo from "../../public/assets/logo.png";
import menu from "../../public/assets/menu.svg"

@connect(({globalModel}) => ({globalModel}))
class AdminNavigation extends Component {
  constructor() {
    super();
    this.state = {
      isOpen: false,
    }
  }
  render() {
    const {globalModel} = this.props;
    const {currentUser} = globalModel;

    const toggle = () => {
      this.state.isopen = !this.state.isopen;
      this.setState(this.state)
    };

    const logOut = () => {
      localStorage.removeItem(TOKEN_NAME);
      this.props.dispatch({
        type: 'globalModel/updateState',
        payload: {
          currentUser: ''
        }
      });
      router.push("/login");

    };
    return (
      <div className="admin-menu">
          <Navbar light expand="md" className="m-0 p-0 ml-3 admin-navbar" >
            <NavbarBrand href="/">
              <img src={logo} alt="" className="img-fluid"  width="180px" height="60px"/>
            </NavbarBrand>
            <Collapse isOpen={this.state.isOpen} navbar>
              <Nav className="ml-5" navbar>
                <NavItem>
                  <NavLink href="">
                    <img src={menu} alt="" width="20px" height="10px" className="img-fluid"/>
                  </NavLink>
                </NavItem>


                {/*<UncontrolledDropdown nav inNavbar>*/}
                {/*  <DropdownToggle nav caret>*/}
                {/*    Options*/}
                {/*  </DropdownToggle>*/}
                {/*  <DropdownMenu>*/}
                {/*    <DropdownItem>*/}
                {/*      Option 1*/}
                {/*    </DropdownItem>*/}
                {/*    <DropdownItem>*/}
                {/*      Option 2*/}
                {/*    </DropdownItem>*/}
                {/*  </DropdownMenu>*/}
                {/*</UncontrolledDropdown>*/}
              </Nav>

              <Nav className="ml-auto" navbar>
                <NavItem>
                  <NavLink href="">1</NavLink>
                </NavItem>
                <NavItem>
                  <NavLink>{currentUser.firstName}</NavLink>
                </NavItem>
              </Nav>
              <Nav className="" navbar>
                <NavItem>
                  {!currentUser ?
                    <NavLink href="/login">Kirish</NavLink>
                    : <NavLink onClick={logOut} style={{cursor:'pointer'}}>Chiqish</NavLink>}
                </NavItem>
              </Nav>
            </Collapse>
          </Navbar>
      </div>
    );
  }
}

AdminNavigation.propTypes = {};

export default AdminNavigation;
