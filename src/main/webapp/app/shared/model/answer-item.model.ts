export interface IAnswerItem {
  id?: number;
  number?: number;
}

export class AnswerItem implements IAnswerItem {
  constructor(public id?: number, public number?: number) {}
}
