package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToOne;

@Entity
@Table(name = "treatments")
public class Treatment implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;
    
    @OneToOne(cascade = CascadeType.MERGE)    
    private Employee employee;
    
    @OneToOne(cascade = CascadeType.MERGE)    
    private Patient patient;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column
    private String diagnostic;

    @Column
    private String observation;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    
    public String getDiagnostic() {
        return diagnostic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    
    
    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }   
    
    
    public Treatment(Employee employee, Patient patient, Date date, String diagnostic, String observation) {
        setEmployee(employee);
        setPatient(patient);
        setDate(date);
        setDiagnostic(diagnostic);
        setObservation(observation);                
    }

    public Treatment(){

    }

}