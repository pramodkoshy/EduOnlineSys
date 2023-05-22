import { IQuestion } from 'app/shared/model/question.model';

export interface IExam {
  id?: number;
  name?: string;
  description?: string;
  courseName?: string;
  courseId?: number;
  questions?: IQuestion[];
}

export class Exam implements IExam {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public courseName?: string,
    public courseId?: number,
    public questions?: IQuestion[]
  ) {}
}
