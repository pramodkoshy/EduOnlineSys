import { Moment } from 'moment';

export interface IExamRegistration {
  id?: number;
  enrolmentDate?: Moment;
  teacherName?: string;
  teacherId?: number;
}

export class ExamRegistration implements IExamRegistration {
  constructor(public id?: number, public enrolmentDate?: Moment, public teacherName?: string, public teacherId?: number) {}
}
