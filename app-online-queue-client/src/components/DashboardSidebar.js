import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {ListGroup, ListGroupItem} from "reactstrap";
import Badge from 'react-bootstrap/Badge';
import {Link} from "react-router-dom";
import {FaUsers, FaUser, FaMapMarkedAlt, FaMicrosoft, FaUserShield, FaUserEdit, FaFileAlt} from 'react-icons/fa';
import {FiMonitor, FiLogOut, FiUserCheck} from 'react-icons/fi';
import {GiBriefcase} from 'react-icons/gi';
import {IoIosMenu} from 'react-icons/io';
import {MdContactMail} from 'react-icons/md';
import {AiOutlineTeam, AiFillDashboard, AiOutlineMenu} from 'react-icons/ai';
import {connect} from "dva";
import ProfilForm from "./ProfilForm";

@connect(({globalModel}) => ({globalModel}))
class DashboardSidebar extends Component {
  render() {
    const {globalModel} = this.props;
    const {currentUser, isAdmin, isModer, isReception, isOperator, isDirector, message} = globalModel;
    return (
      <div className="katalog-sidebar">
        <div className="menuSidebar">
          <div className="userStatus text-center text-white">
            <h5 className="">{currentUser.firstName}</h5>
            <div className="m-auto w-25">
              <h6>Online</h6>
            </div>
          </div>
          <ListGroup className="">
            <ListGroupItem className="">
              <Link to="/"
                    className={window.location.pathname === "/" ? "active-katalog nav-link" : "nav-link"}>
                <AiFillDashboard className="list-group-item-icon"/> Dashboard</Link>
            </ListGroupItem>
            <ListGroupItem className={isAdmin ? "d-block" : "d-none"}>
              <Link to="/admin/category"
                    className={window.location.pathname === "/" ? "active-katalog nav-link" : "nav-link"}>
                <IoIosMenu className="list-group-item-icon"/> Kategoriyalar</Link>
            </ListGroupItem>
            <ListGroupItem className={isAdmin ? "d-block" : "d-none"}>
              <Link to="/maps"
                    className={window.location.pathname === "/" ? "active-katalog nav-link" : "nav-link"}>
                <FaMapMarkedAlt className="list-group-item-icon"/> Hududlar</Link>
            </ListGroupItem>
            <ListGroupItem className={isAdmin ? "d-block" : "d-none"}>
              <Link to="/moderator"
                    className={window.location.pathname === "/" ? "active-katalog nav-link" : "nav-link"}>
                <FaUserShield className="list-group-item-icon"/> Moderatorlar</Link>
            </ListGroupItem>
            <ListGroupItem className={isAdmin|| isModer ? "d-block" : "d-none"}>
              <Link to="/company"
                    className={window.location.pathname === "/company" ? "active-katalog nav-link" : "nav-link"}>
                <GiBriefcase className="list-group-item-icon"/> Kompaniyalar</Link>
            </ListGroupItem>
            <ListGroupItem className={isDirector ? "d-block" : "d-none"}>
              <Link to="/direction"
                    className={window.location.pathname === "/" ? "active-katalog nav-link" : "nav-link"}>
                <FaMicrosoft className="list-group-item-icon"/> Bo'limlar</Link>
            </ListGroupItem>
            <ListGroupItem className={isDirector ? "d-block" : "d-none"}>
              <Link to="/direction"
                    className={window.location.pathname === "/" ? "active-katalog nav-link" : "nav-link"}>
                <FiUserCheck className="list-group-item-icon"/> Receptionlar</Link>
            </ListGroupItem>
            <ListGroupItem className={isDirector || isReception ? "d-block" : "d-none"}>
              <Link to="/moderator"
                    className={window.location.pathname === "/operator" ? "active-katalog nav-link" : "nav-link"}>
                <FaUsers className="list-group-item-icon"/> Operatorlar</Link>
            </ListGroupItem>
            <ListGroupItem className={!(isAdmin||isModer) ? "d-block" : "d-none"}>
              <Link to="/reception"
                    className={window.location.pathname === "/reception" ? "active-katalog nav-link" : "nav-link"}>
                <AiOutlineTeam className="list-group-item-icon"/> Navbatlar</Link>
            </ListGroupItem>
            <ListGroupItem className={!isAdmin ? "d-block" : "d-none"}>
              <Link to="/kiosk"
                    className={window.location.pathname === "/operator" ? "active-katalog nav-link" : "nav-link"}><FiMonitor
                className="list-group-item-icon"/> Kiosk</Link>

            </ListGroupItem>
            <ListGroupItem>
              <Link to="/profil"
                    className={window.location.pathname === "/profil" ? "active-katalog nav-link" : "nav-link"}>
                <FaUserEdit className="list-group-item-icon"/>  Profil</Link>
            </ListGroupItem>

            <ListGroupItem>
              <Link to="/message"
                    className={window.location.pathname === "/message" ? "active-katalog nav-link" : "nav-link"}>
                <FaUserEdit className="list-group-item-icon"/> Messages</Link>
            </ListGroupItem>
            <ListGroupItem className={isAdmin ? "d-block" : "d-none"}>
              <Link to="/users"
                    className={window.location.pathname === "/operator" ? "active-katalog nav-link" : "nav-link"}>
                <FaUser className="list-group-item-icon"/> Foydalanuvchilar</Link>
            </ListGroupItem>
            <ListGroupItem className={isAdmin || isDirector ? "d-block" : "d-none"}>
              <Link to=""
                    className={window.location.pathname === "/operator" ? "active-katalog nav-link" : "nav-link"}>
                <MdContactMail className="list-group-item-icon"/>
                {isAdmin  ? " Bog'lanish " : " Bildirishnoma "}
                <Badge className="ml-sm-1 ml-md-1 " variant={!message ? "danger"
                  : "primary"}> {message}</Badge>
              </Link>
            </ListGroupItem>
            <ListGroupItem className={isAdmin ? "d-block" : "d-none"}>
              <Link to=""
                    className={window.location.pathname === "/operator" ? "active-katalog nav-link" : "nav-link"}>
                <FaFileAlt className="list-group-item-icon"/> Hisobot</Link>
            </ListGroupItem>
          </ListGroup>
        </div>
      </div>
    );
  }
}

DashboardSidebar.propTypes = {};

export default DashboardSidebar;
