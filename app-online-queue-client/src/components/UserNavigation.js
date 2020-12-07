import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {
  Collapse,
  Container, DropdownItem, DropdownMenu, DropdownToggle,
  Nav,
  Navbar,
  NavbarBrand,
  NavbarToggler,
  NavItem,
  NavLink,
  UncontrolledDropdown
} from "reactstrap";
import logo from "../../public/assets/logo.png";
class UserNavigation extends Component {
  render() {
    return (
      <div className="admin-menu">
          <Navbar light expand="md">
            <NavbarBrand href="">
              <img src={logo} alt="" className="img-fluid"  width="180px" height="60px"/>
            </NavbarBrand>

            <Collapse  navbar>

              <Nav className="ml-5" navbar>
                <NavItem>
                  <NavLink href="/">Home</NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/sells">Features</NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/catalog/brand">Contact</NavLink>
                </NavItem>
              </Nav>
              <Nav className="ml-auto" navbar>
                <NavItem>
                  <NavLink>Menu</NavLink>
                </NavItem>
              </Nav>

            </Collapse>
          </Navbar>
      </div>
    );
  }
}

UserNavigation.propTypes = {};

export default UserNavigation;
