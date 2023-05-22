import { Moment } from 'moment';

export interface ICourseEnrolment {
  id?: number;
  enrolmentDate?: Moment;
  studentName?: string;
  studentId?: number;
}

export class CourseEnrolment implements ICourseEnrolment {
  constructor(public id?: number, public enrolmentDate?: Moment, public studentName?: string, public studentId?: number) {}
}
