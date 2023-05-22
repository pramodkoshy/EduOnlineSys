import { IQuestionItem } from 'app/shared/model/question-item.model';

export interface IQuestion {
  id?: number;
  name?: string;
  description?: string;
  number?: number;
  questionItems?: IQuestionItem[];
  examName?: string;
  examId?: number;
}

export class Question implements IQuestion {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public number?: number,
    public questionItems?: IQuestionItem[],
    public examName?: string,
    public examId?: number
  ) {}
}
